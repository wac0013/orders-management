package br.com.foursales.app.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.foursales.app.domain.model.UserEntity;

@Configuration
@EnableElasticsearchRepositories
@EnableElasticsearchAuditing(auditorAwareRef = "auditorUserIdProvider")
public class ElasticsearchAuditConfig {



	@Bean(name = "auditorUserIdProvider")
	public AuditorAware<UUID> auditorUserIdProvider() {
		return new AuditorAware<UUID>() {
			@Override
			public Optional<UUID> getCurrentAuditor() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

				if (authentication == null || !authentication.isAuthenticated()) {
					return Optional.empty();
				}

				try {
					var user = (UserEntity) authentication.getPrincipal();
					return Optional.of(user.getId());
				} catch (ClassCastException e) {
					return Optional.empty();
				}
			}
		};
	}
}
