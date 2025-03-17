package br.com.foursales.app.application.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Builder;

@Builder
public record OrderCreateRequest(
    BigDecimal discount,
    Set<OrderItemCreate> items,
    PaymentRequest payment
) {}
