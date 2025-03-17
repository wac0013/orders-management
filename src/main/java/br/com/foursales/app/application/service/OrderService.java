package br.com.foursales.app.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.foursales.app.application.dto.OrderCreateRequest;
import br.com.foursales.app.application.dto.OrderItemResponse;
import br.com.foursales.app.application.dto.OrderResponse;
import br.com.foursales.app.domain.document.ProductDocument;
import br.com.foursales.app.domain.enums.OrderStatusEnum;
import br.com.foursales.app.domain.model.OrderEntity;
import br.com.foursales.app.domain.model.OrderItemEntity;
import br.com.foursales.app.domain.model.PaymentEntity;
import br.com.foursales.app.domain.repository.OrderRepository;
import br.com.foursales.app.domain.repository.ProductRepository;
import br.com.foursales.app.infrastructure.messaging.KafkaProducer;
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
	public OrderResponse create(OrderCreateRequest order) {
		var products = checkAllProductsExists(order.items()
			.stream()
			.map(item -> item.productId())
			.toList());
		var orderEntity = mapToEntity(order, products);
		var orderSaved = repository.save(orderEntity);

		sendMessageToKafka(orderCreatedTopic, orderSaved);
		return mapToResponse(orderSaved);
	}

	private OrderResponse mapToResponse(OrderEntity order) {
		return OrderResponse.builder()
			.id(order.getId().toString())
			.status(order.getStatus())
			.total(order.getTotal())
			.discount(order.getDiscount())
			.items(order.getItems().parallelStream()
				.map(item -> {
					return OrderItemResponse.builder()
						.sequence(item.getSequence())
						.productId(item.getProduct())
						.amount(item.getAmount())
						.unitaryPrice(item.getUnitaryPrice())
						.build();
				}).toList())
			.build();
	}

	private OrderEntity mapToEntity(OrderCreateRequest order, List<ProductDocument> products) {
		var orderEntity = mapper.map(order, OrderEntity.class);
		var items = new HashSet<OrderItemEntity>();
		int index = 1;
		for (var item : order.items()) {
			var orderItemEntity = mapper.map(item, OrderItemEntity.class);
			var productOptional = products.stream()
					.filter(p -> p.getId().equals(item.productId()))
					.findFirst();
			orderItemEntity.setSequence((short) index++);
			orderItemEntity.setAmount(Long.valueOf(item.amount()));
			orderItemEntity.setProduct(productOptional.get().getId());
			orderItemEntity.setUnitaryPrice(productOptional.get().getPrice());
			orderItemEntity.setOrder(orderEntity);
			items.add(orderItemEntity);
		}

		orderEntity.setItems(items);

		BigDecimal total = items.stream()
			.map(item -> {
				return BigDecimal.valueOf(item.getAmount()).multiply(item.getUnitaryPrice());
			})
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(4, RoundingMode.HALF_UP);

		var payment = mapper.map(order.payment(), PaymentEntity.class);
		payment.setValue(total.min(orderEntity.getDiscount()));
		payment.setOrder(orderEntity);

		orderEntity.setTotal(total);
		orderEntity.setPayments(Set.of(payment));

		return orderEntity;
	}

	private List<ProductDocument> checkAllProductsExists(List<String> productsIds) {
		var products = productRepository.findAllByIdInAndStockGreaterThanZero(productsIds);
		var notFoundProducts = productsIds.stream()
			.filter(productId -> products.stream().noneMatch(p -> p.getId().equals(productId)))
			.toList();

		if (!notFoundProducts.isEmpty()) {
			throw new NotFoundException(MessageFormat.format("Produtos com estoque não encontrados: {0}", notFoundProducts));
		}

		return products;
	}

	@Transactional
	public void doPayment(UUID orderId) {
		var orderOptional = repository.findByIdAndStatusIn(orderId, List.of(OrderStatusEnum.PENDING));

		if (orderOptional.isEmpty()) {
			throw new NotFoundException(MessageFormat.format("Pedido pendente não encontrado: {0}", orderId));
		}

		paymentService.processPayment(orderId);

		var order = orderOptional.get();
		order.setStatus(OrderStatusEnum.PAID);
		repository.save(order);
		sendMessageToKafka(orderPaidTopic, order.getId().toString());
	}

	private void sendMessageToKafka(String topic, Object payload) {
		try {
			kafkaProducer.sendMessage(topic, payload);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void processOrderPaymentAndStock(UUID orderId) {
		var order = repository.findById(orderId)
			.orElseThrow(() ->
				new NotFoundException(MessageFormat.format("Pedido não encontrado: {0}", orderId)));

		var productIds = order.getItems().stream()
			.map(item -> item.getProduct())
			.toList();

		var availableProducts = productRepository.findAllByIdInAndStockGreaterThanZero(productIds);
		var unavailableProducts = productIds.stream()
			.filter(productId -> availableProducts.stream().noneMatch(p -> p.getId().equals(productId)))
			.toList();

		order.setStatus(!unavailableProducts.isEmpty() ? OrderStatusEnum.CANCELED : OrderStatusEnum.PAID);
		repository.save(order);
	}
}
