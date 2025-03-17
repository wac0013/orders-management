package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.foursales.app.application.dto.OrderCreateRequest;
import br.com.foursales.app.application.service.OrderService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createOrder(@RequestBody OrderCreateRequest newOrder) {
		orderService.create(newOrder);
	}

	@PatchMapping("/{id}/payments/installment/{installment}")
	public void payOrder(@RequestBody String entity, @RequestParam String id, @RequestParam Short installment) {
		orderService.doPayment(UUID.fromString(id), installment);
	}


}
