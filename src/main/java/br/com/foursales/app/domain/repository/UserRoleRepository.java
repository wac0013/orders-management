package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.UUID;

import br.com.foursales.app.domain.model.UserRoleEntity;

public interface UserRoleRepository extends UuidIdentifierRepository<UserRoleEntity> {

	List<UserRoleEntity> findByUserId(UUID userId);
}
