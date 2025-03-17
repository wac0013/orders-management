package br.com.foursales.app.infrastructure.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ConfigurationProperties(prefix = "foursales.app")
public record AppConfig(@NotNull JwtConfig jwt) {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class JwtConfig {
		@NotBlank
		private String secret;
		private Duration expiration = Duration.ofHours(3);
		private Duration refreshExpiration = Duration.ofHours(72);
	}
}
