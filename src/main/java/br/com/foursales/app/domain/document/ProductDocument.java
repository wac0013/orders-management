package br.com.foursales.app.domain.document;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class ProductDocument  {

	@Id
	private String id;

	@Field
	private String name;

	@Field
	private String description;

	@Field
	@Builder.Default
	private Long currentStock = 0L;

	@Field
	@Builder.Default
	private BigDecimal price = BigDecimal.ZERO;

}
