package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	@GetMapping("/top-buyers")
	public String getTopBuyers(@RequestParam String param) {
		return new String();
	}

	@GetMapping("/average-ticket")
	public String getAverageTicket(@RequestParam String param) {
		// Logic to calculate the average ticket will be implemented here
		return new String();
	}

	@GetMapping("/monthly-revenue")
	public String getMonthlyRevenue(@RequestParam(required = false) String month) {
		// Logic to calculate the revenue for the specified month
		return "";
	}


}
