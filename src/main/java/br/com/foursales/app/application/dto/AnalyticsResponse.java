package br.com.foursales.app.application.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record AnalyticsResponse(
	List<TopBuyers> topBuyers,
	BigDecimal monthlyRevenue,
	BigDecimal avgTicket
) {

	@Builder
	public static record TopBuyers(String id, Long amount) {}
}
