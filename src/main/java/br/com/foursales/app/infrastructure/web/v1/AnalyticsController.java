package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.foursales.app.application.service.AnalyticService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	private final AnalyticService service;

	@GetMapping("/top-buyers")
	public String getTopBuyers(@RequestParam String param) {
		return service.getTopBuyers();
	}

	@GetMapping("/average-ticket")
	public BigDecimal getAverageTicket(@RequestParam String param) {
		return service.getAvgTicket();
	}

	@GetMapping("/monthly-revenue")
	public BigDecimal getMonthlyRevenue(@RequestParam(required = false) String month) {
		return service.getMonthlyRevenue(month);
	}


}
