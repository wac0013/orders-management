package br.com.foursales.app.application.dto;

import lombok.Builder;

@Builder
public record OrderItemCreate(String productId, Integer amount) {

}
