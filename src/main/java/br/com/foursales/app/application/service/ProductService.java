package br.com.foursales.app.application.service;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.foursales.app.application.dto.ProductCreateRequest;
import br.com.foursales.app.application.dto.ProductResponse;
import br.com.foursales.app.domain.document.ProductDocument;
import br.com.foursales.app.domain.repository.ProductRepository;
import br.com.foursales.app.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;
	private final ModelMapper mapper;


	public Page<ProductResponse> findAll(PageRequest pageable) {
		return repository.findByCurrentStockGreaterThan(0L, pageable).map(this::convertToResponse);
	}

	public ProductResponse create(ProductCreateRequest productRequest) {
		var document = mapper.map(productRequest, ProductDocument.class);
		return convertToResponse(repository.save(document));
	}

	public ProductResponse update(String id, ProductCreateRequest productRequest) {
		var document = findByIdOrThrow(id);
		mapper.map(productRequest, document);
		return convertToResponse(repository.save(document));
	}

	public void delete(String id) {
		var document = findByIdOrThrow(id);
		repository.delete(document);;
	}

	private ProductDocument findByIdOrThrow(String id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(
			MessageFormat.format("Produto com id {0} não encontrado", id)
		));
	}

	private ProductResponse convertToResponse(ProductDocument document) {
		return ProductResponse.builder()
			.id(document.getId())
			.name(document.getName())
			.price(document.getPrice())
			.description(document.getDescription())
			.currentStock(document.getCurrentStock())
			.build();
	}

}
