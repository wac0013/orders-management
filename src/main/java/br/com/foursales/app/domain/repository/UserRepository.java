package br.com.foursales.app.domain.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.model.UserEntity;

@Repository
public interface UserRepository extends UuidIdentifierRepository<UserEntity> {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String email);
}
