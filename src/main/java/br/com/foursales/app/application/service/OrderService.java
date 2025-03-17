package br.com.foursales.app.application.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.foursales.app.application.dto.OrderCreateRequest;
import br.com.foursales.app.domain.enums.OrderStatusEnum;
import br.com.foursales.app.domain.model.OrderEntity;
import br.com.foursales.app.domain.repository.OrderRepository;
import br.com.foursales.app.domain.repository.ProductRepository;
import br.com.foursales.app.infrastructure.messaging.KafkaProducer;
import br.com.foursales.app.utils.exception.BusinessException;
import br.com.foursales.app.utils.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final ModelMapper mapper;
	private final OrderRepository repository;
	private final ProductRepository productRepository;
	private final KafkaProducer kafkaProducer;
	private final PaymentService paymentService;

	@Value("${foursales.app.messaging.topic.order.created}") private String orderCreatedTopic;
	@Value("${foursales.app.messaging.topic.order.paid}") private String orderPaidTopic;


	@Transactional
	public OrderEntity create(OrderCreateRequest order) {
		checkAllProductsExists(order.items()
			.stream()
			.map(item -> item.productId())
			.toList());
		var orderEntity = mapper.map(order, OrderEntity.class);
		var orderSaved = repository.save(orderEntity);

		kafkaProducer.sendMessage(orderCreatedTopic, mapper.map(orderSaved, String.class));
		return orderSaved;
	}

	private void checkAllProductsExists(List<String> productsIds) {
		var products = productRepository.findAllByIdIn(productsIds);

		var notFoundProducts = new ArrayList<String>();
		var outOfStockProducts = new ArrayList<String>();

		for (String productId : productsIds) {
			var productOptional = products.stream()
				.filter(p -> p.getId().equals(productId))
				.findFirst();

			if (productOptional.isEmpty()) {
				notFoundProducts.add(productId);
			} else if (productOptional.get().getCurrentStock() <= 0) {
				outOfStockProducts.add(productId);
			}
		}

		if (!notFoundProducts.isEmpty()) {
			throw new NotFoundException(MessageFormat.format("Produtos não encontrados: {0}", notFoundProducts));
		}

		if (!outOfStockProducts.isEmpty()) {
			throw new BusinessException(MessageFormat.format("Produtos sem estoque: {0}", outOfStockProducts));
		}
	}

	@Transactional
	public void doPayment(UUID orderId, Short installment) {
		var orderOptional = repository.findByIdAndStatusIn(orderId, List.of(OrderStatusEnum.PENDING));

		if (orderOptional.isEmpty()) {
			throw new NotFoundException(MessageFormat.format("Pedido pendente não encontrado: {0}", orderId));
		}

		paymentService.processPayment(orderId, installment);

		var order = orderOptional.get();
		order.setStatus(OrderStatusEnum.PAID);
		repository.save(order);
		kafkaProducer.sendMessage(orderPaidTopic, mapper.map(order, String.class));
	}

}
