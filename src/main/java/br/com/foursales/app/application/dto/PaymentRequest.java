package br.com.foursales.app.application.dto;

import br.com.foursales.app.domain.enums.PaymentTypeEnum;
import lombok.Builder;

@Builder
public record PaymentRequest(
	PaymentTypeEnum type,
    Short installments
) {}
