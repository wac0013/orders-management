package br.com.foursales.app.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "orders_items")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OrderItemEntity extends UuidIdentifierEntity{

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	@Column(nullable = false)
	private String product;

	@Positive()
	@Column(nullable = false)
	private short sequence;

	@Positive()
	@Column(nullable = false)
	private Long amount;

	@Positive
	@Column(nullable = false, precision = 10, scale = 4)
	private BigDecimal unitaryPrice;
}
