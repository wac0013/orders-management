package br.com.foursales.app.infrastructure.messaging;

import org.springframework.stereotype.Service;

import br.com.foursales.app.application.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
	private final OrderService orderService;

	@KafkaListener(id = "order-created", groupId = "${foursales.app.messaging.consumer.group-id: foursales-group}",
		topics = "${foursales.app.messaging.topic.order.created}")
	public void listenOrderCreated(@Payload String message, ConsumerRecordMetadata meta) {
		logger.debug(message);
	}

	@KafkaListener(id = "order-paid", groupId = "${foursales.app.messaging.consumer.group-id: foursales-group}",
		topics = "${foursales.app.messaging.topic.order.paid}")
	public void listenOrderPaid(@Payload String message, ConsumerRecordMetadata meta) {
		logger.debug(message);
		orderService.processOrderPaymentAndStock(UUID.fromString(message));
	}

}
