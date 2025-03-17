package br.com.foursales.app.infrastructure.messaging;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@EnableKafka
@RequiredArgsConstructor
public class KafkaProducer {

	private ObjectMapper mapper;
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String topic, Object message) throws JsonProcessingException {
        kafkaTemplate.send(topic, mapper.writeValueAsString(message));
    }

}
