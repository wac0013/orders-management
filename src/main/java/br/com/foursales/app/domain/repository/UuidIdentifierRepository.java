package br.com.foursales.app.domain.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.foursales.app.domain.model.UuidIdentifierEntity;

@NoRepositoryBean
public interface UuidIdentifierRepository<E extends UuidIdentifierEntity> extends CrudRepository<E, UUID> {

}
