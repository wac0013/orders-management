package br.com.foursales.app.application.dto;

import java.math.BigDecimal;


public record ProductCreateRequest(
	String name,
	String description,
	Long currentStock,
	BigDecimal price
) {}
