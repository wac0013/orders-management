package br.com.foursales.app.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.foursales.app.domain.model.LongIdentifierEntity;

@NoRepositoryBean
public interface LongIdentifierRepository<E extends LongIdentifierEntity> extends CrudRepository<E, Long> {

}
