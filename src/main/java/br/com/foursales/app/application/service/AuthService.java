package br.com.foursales.app.application.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.foursales.app.application.dto.JwtTokenResponse;
import br.com.foursales.app.application.dto.LoginRequest;
import br.com.foursales.app.domain.model.UserEntity;
import br.com.foursales.app.domain.repository.UserRoleRepository;
import br.com.foursales.app.infrastructure.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AppConfig config;
	private final UserService userService;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;

	private SecretKey getKey() {
		try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(config.jwt().getSecret().getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(hash); // Sempre terá 256 bits (32 bytes)
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar a chave JWT", e);
        }
	}

	public String generateToken(UserEntity user, boolean isRefresh) {
		var now = new Date();
		var timeToAdd = isRefresh
			? config.jwt().getRefreshExpiration().toMillis()
			: config.jwt().getExpiration().toMillis();
        var expiryDate = new Date(now.getTime() + timeToAdd);

		return Jwts.builder()
			.subject(user.getId().toString())
			.claim("username", user.getUsername())
			.claim("email", user.getEmail())
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(this.getKey())
			.compact();
	}

    public boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser()
				.verifyWith(this.getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();

            var now = new Date();
            if (claims.getExpiration().before(now)) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
				.verifyWith(this.getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();

        var id = claims.getSubject();
		return UUID.fromString(id);
    }

	public UserEntity getUserFromJWT(String token) {
		var id = getUserIdFromJWT(token);
		return userService.findByIdOrThrow(id);
	}

	public JwtTokenResponse doLogin(LoginRequest loginRequest) {
		var user = userService.loadUserByUsername(loginRequest.username());
		validatePassword(loginRequest.password(), user.getPassword());

		return generateJwtTokenResponse(user);
	}

	public JwtTokenResponse refreshToken(String refreshToken) {
		if (!isValidToken(refreshToken)) {
			throw new BadCredentialsException("Token inválido");
		}

		var user = getUserFromJWT(refreshToken);

		return generateJwtTokenResponse(user);
	}

	private JwtTokenResponse generateJwtTokenResponse(UserEntity user) {
		var token = generateToken(user, false);
		var refreshToken = generateToken(user, true);

		return JwtTokenResponse.builder()
			.accessToken(token)
			.refreshToken(refreshToken)
			.build();
	}

	private void validatePassword(String password, String encodedPassword) {
		if (!passwordEncoder.matches(password, encodedPassword)) {
			throw new BadCredentialsException("Credenciais inválidas");
		}
	}

	public List<SimpleGrantedAuthority> getRolesByUserId(UUID userId) {
		return userRoleRepository
			.findByUserId(userId)
			.stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
			.toList();
	}
}
