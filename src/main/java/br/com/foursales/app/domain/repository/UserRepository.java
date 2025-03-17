package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.foursales.app.application.dto.AnalyticsResponse;
import br.com.foursales.app.domain.model.UserEntity;

@Repository
public interface UserRepository extends UuidIdentifierRepository<UserEntity> {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String email);

	@Query(value = """
		SELECT new br.com.foursales.app.application.dto.AnalyticsResponse.TopBuyers(u.id, COUNT(o))
		FROM orders o
		INNER JOIN users u ON u.id = o.created_by
		WHERE o.status = 'PAID'
		GROUP BY u.id
		ORDER BY COUNT(o) DESC
	""", nativeQuery = true)
	List<AnalyticsResponse.TopBuyers> findTopBuyers();
}
