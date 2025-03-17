package br.com.foursales.app.application.dto;

import java.math.BigDecimal;
import br.com.foursales.app.domain.enums.PaymentStatusEnum;
import br.com.foursales.app.domain.enums.PaymentTypeEnum;

public record PaymentRequest(
    String orderId,
    PaymentTypeEnum type,
    BigDecimal value,
    PaymentStatusEnum status,
    Short installment
) {}
