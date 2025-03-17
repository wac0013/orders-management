package br.com.foursales.app.domain.document;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@environment.getProperty('foursales.app.elasticsearch.index-prefix', '')}products")
public class ProductDocument  extends AuditableDocument {

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
