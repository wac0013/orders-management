package br.com.foursales.app.infrastructure.persistence;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.foursales.app.domain.model.UserEntity;

@EnableJpaAuditing(auditorAwareRef = "auditorUserProvider")
@Configuration
public class JpaAuditConfig {

    @Bean(name = "auditorUserProvider")
	public AuditorAware<UserEntity> auditorUserIdProvider() {
		return new AuditorAware<UserEntity>() {
			@Override
			public Optional<UserEntity> getCurrentAuditor() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

				if (authentication == null || !authentication.isAuthenticated()) {
					return Optional.empty();
				}

				try {
					return Optional.of((UserEntity) authentication.getPrincipal());
				} catch (ClassCastException e) {
					return Optional.empty();
				}
			}
		};
	}
}
