package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.foursales.app.application.dto.CreateUserRequest;
import br.com.foursales.app.application.dto.JwtTokenResponse;
import br.com.foursales.app.application.dto.LoginRequest;
import br.com.foursales.app.application.service.AuthService;
import br.com.foursales.app.application.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@Validated
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService service;
	private final AuthService authService;

	@PostMapping("/login")
	public JwtTokenResponse login(@RequestBody LoginRequest body) {
		return authService.doLogin(body);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public UUID signup(@RequestBody CreateUserRequest body) {
		return service.create(body).getId();
	}

	@GetMapping("/refresh")
	public JwtTokenResponse refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
		String refreshToken = authorizationHeader.replace("Bearer ", "");
		return authService.refreshToken(refreshToken);
	}

}
