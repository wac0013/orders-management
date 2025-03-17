
package br.com.foursales.app.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.enums.OrderStatusEnum;
import br.com.foursales.app.domain.model.OrderEntity;

@Repository
public interface OrderRepository  extends UuidIdentifierRepository<OrderEntity> {

	Optional<OrderEntity> findByIdAndStatusIn(UUID id, List<OrderStatusEnum> status);

	@Query(value = """
		SELECT SUM(o.total)
		FROM orders o
		inner join payments p on o.id = p.order_id and p.status = 'PAID'
		WHERE o.status = 'PAID'
		AND MONTH(p.updated_at) = MONTH(CURRENT_DATE)
		AND YEAR(p.updated_at) = YEAR(CURRENT_DATE)
	""", nativeQuery = true)
	BigDecimal getMonthlyRevenue();

	@Query("""
		SELECT AVG(o.total)
		FROM OrderEntity o
		WHERE o.status = 'PAID'
	""")
	BigDecimal getAvgTicket();
}
