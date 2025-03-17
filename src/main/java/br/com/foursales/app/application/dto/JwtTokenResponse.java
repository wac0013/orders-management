package br.com.foursales.app.application.dto;

import lombok.Builder;

@Builder
public record JwtTokenResponse(String accessToken, String refreshToken) {

}
