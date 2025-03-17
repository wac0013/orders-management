package br.com.foursales.app.infrastructure.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.foursales.app.domain.model.UserEntity;
import br.com.foursales.app.infrastructure.config.SecurityAuditorAware;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class JPAConfig {

    @Bean
    public AuditorAware<UserEntity> auditorProvider() {
        return new SecurityAuditorAware();
    }
}
