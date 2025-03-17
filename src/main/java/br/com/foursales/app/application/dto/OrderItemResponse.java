package br.com.foursales.app.application.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record OrderItemResponse(
	Short sequence,
	String productId,
	Long amount,
	BigDecimal unitaryPrice) {

}
