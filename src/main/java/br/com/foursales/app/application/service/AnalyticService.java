package br.com.foursales.app.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.foursales.app.domain.repository.OrderRepository;
import br.com.foursales.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticService {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;


  public String getTopBuyers() {
    return userRepository.findTopBuyers().toString();
  }

  public BigDecimal getAvgTicket() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAvgTicket'");
  }

  public BigDecimal getMonthlyRevenue(String month) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMonthlyRevenue'");
  }

}
