package br.com.foursales.app.application.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProductResponse(String id, String name, String description, Long currentStock, BigDecimal price) {

}
