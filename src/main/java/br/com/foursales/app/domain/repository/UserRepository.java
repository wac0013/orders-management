package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.model.UserEntity;

@Repository
public interface UserRepository extends UuidIdentifierRepository<UserEntity> {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String email);

	@Query("SELECT u FROM UserEntity u WHERE u.orders.size > 0 ORDER BY u.orders.size DESC LIMIT 5")
	List<UserEntity> findTopBuyers();
}
