package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.enums.PaymentStatusEnum;
import br.com.foursales.app.domain.model.PaymentEntity;

@Repository
public interface PaymentRepository extends UuidIdentifierRepository<PaymentEntity> {

	Optional<PaymentEntity> findByOrderIdAndInstallmentAndStatusIn(UUID orderId, short installment, List<PaymentStatusEnum> status);

}
