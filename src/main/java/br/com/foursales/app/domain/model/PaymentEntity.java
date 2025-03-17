package br.com.foursales.app.domain.model;

import java.math.BigDecimal;

import br.com.foursales.app.domain.enums.PaymentStatusEnum;
import br.com.foursales.app.domain.enums.PaymentTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity extends UuidIdentifierEntity {

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	@Enumerated(EnumType.STRING)
	private PaymentTypeEnum type;

	@Builder.Default
	@Column(nullable = false, precision = 10, scale = 4)
	private BigDecimal value = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	private PaymentStatusEnum status;

	@Positive
	@Builder.Default
	@Column(nullable = false)
	private Short installments = 1;
}
