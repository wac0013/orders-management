package br.com.foursales.app.infrastructure.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.foursales.app.domain.model.UserEntity;

public class SecurityAuditorAware implements AuditorAware<UserEntity>{

  @Override
  public Optional<UserEntity> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity) {
            return Optional.of((UserEntity) principal);
        } else {
            return Optional.empty();
        }
  }

}
