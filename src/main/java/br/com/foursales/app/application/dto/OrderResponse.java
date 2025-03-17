package br.com.foursales.app.application.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.foursales.app.domain.enums.OrderStatusEnum;
import lombok.Builder;

@Builder
public record OrderResponse(
	String id,
	OrderStatusEnum status,
	BigDecimal total,
	BigDecimal discount,
	List<OrderItemResponse> items,
	PaymentResponse payment
) {

}
