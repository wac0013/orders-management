package br.com.foursales.app.application.dto;

import java.util.Date;

import lombok.Builder;

@Builder
public record ExceptionHandlerResponse(
	String message,
	short httpCode,
	short errorCode,
	Date timestamp,
	String path) {}
