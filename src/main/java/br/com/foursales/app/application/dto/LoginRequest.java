package br.com.foursales.app.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
    @NotBlank(message = "Username or email cannot be blank")
    String username,

    @NotBlank(message = "Password cannot be blank")
    String password
) {
}
