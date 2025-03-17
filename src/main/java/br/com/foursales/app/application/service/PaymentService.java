package br.com.foursales.app.application.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.foursales.app.domain.enums.PaymentStatusEnum;
import br.com.foursales.app.domain.repository.PaymentRepository;
import br.com.foursales.app.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository repository;


	public void processPayment(UUID orderId) {
		var paymentOptional = repository.findByOrderIdAndStatusIn(
			orderId, List.of(PaymentStatusEnum.PENDING));

		if (paymentOptional.isEmpty()) {
			throw new NotFoundException(MessageFormat.format(
					"Pagamento pendente para o pedido {0} e parcela {1} não encontrado", orderId));
		}

		var payment = paymentOptional.get();
		payment.setStatus(PaymentStatusEnum.PAID);
		repository.save(payment);
	}
}
