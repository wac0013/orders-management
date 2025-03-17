package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.foursales.app.application.dto.AnalyticsResponse;
import br.com.foursales.app.application.service.AnalyticService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	private final AnalyticService service;

	@GetMapping("/analytics")
	public AnalyticsResponse getAnalytics(
		@RequestParam(required = false, defaultValue = "false") boolean topBuyers,
		@RequestParam(required = false, defaultValue = "false") boolean averageTicket,
		@RequestParam(required = false, defaultValue = "false") boolean monthlyRevenue) {

		return AnalyticsResponse.builder()
			.topBuyers(topBuyers ? service.getTopBuyers() : null)
			.avgTicket(averageTicket ? service.getAvgTicket() : null)
			.monthlyRevenue(monthlyRevenue ? service.getMonthlyRevenue() : null)
			.build();
	}

}
