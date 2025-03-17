package br.com.foursales.app.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import br.com.foursales.app.OrdersApplication;
import br.com.foursales.app.application.dto.AnalyticsResponse;
import br.com.foursales.app.domain.repository.OrderRepository;
import br.com.foursales.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticService {

    private final OrdersApplication ordersApplication;

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

  public List<AnalyticsResponse.TopBuyers> getTopBuyers() {
    return userRepository.findTopBuyers();
  }

  public BigDecimal getAvgTicket() {
    return orderRepository.getAvgTicket();
  }

  public BigDecimal getMonthlyRevenue() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMonthlyRevenue'");
  }

}
