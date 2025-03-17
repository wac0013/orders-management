package br.com.foursales.app.domain.document;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class AuditableDocument {
    @CreatedDate
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Instant createdAt;

    @LastModifiedDate
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Instant updatedAt;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;
}
