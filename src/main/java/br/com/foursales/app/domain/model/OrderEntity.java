package br.com.foursales.app.domain.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import br.com.foursales.app.domain.enums.OrderStatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "orders")
public class OrderEntity extends UuidIdentifierEntity{

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private OrderStatusEnum status = OrderStatusEnum.PENDING;

	@Builder.Default
	@Column(nullable = false, precision = 10, scale = 4)
	private BigDecimal total = BigDecimal.ZERO;

	@Builder.Default
	@Column(nullable = false, precision = 10, scale = 4)
	private BigDecimal discount = BigDecimal.ZERO;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	@Builder.Default
	private Set<OrderItemEntity> items = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	@Builder.Default
	private Set<PaymentEntity> payments = new HashSet<>();
}
