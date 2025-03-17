package br.com.foursales.app.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import br.com.foursales.app.domain.document.ProductDocument;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductDocument, String> {

	Page<ProductDocument> findByCurrentStockGreaterThan(Long value, Pageable pageable);

	Optional<ProductDocument> findById(String id);

	@Query("{\"bool\": {\"must\": [{\"terms\": {\"_id\": ?0}}, {\"range\": {\"currentStock\": {\"gt\": 0}}}]}}")
	List<ProductDocument> findAllByIdInAndStockGreaterThanZero(List<String> ids);
}
