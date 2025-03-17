
package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.enums.OrderStatusEnum;
import br.com.foursales.app.domain.model.OrderEntity;

@Repository
public interface OrderRepository  extends UuidIdentifierRepository<OrderEntity> {

	Optional<OrderEntity> findByIdAndStatusIn(UUID id, List<OrderStatusEnum> status);
}
