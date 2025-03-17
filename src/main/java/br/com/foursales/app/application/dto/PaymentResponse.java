package br.com.foursales.app.application.dto;

import java.math.BigDecimal;

import br.com.foursales.app.domain.enums.PaymentStatusEnum;
import lombok.Builder;

@Builder
public record PaymentResponse(
	String id,
	BigDecimal value,
	PaymentStatusEnum status) {
}
