package br.com.foursales.app.domain.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Getter
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UuidIdentifierEntity extends AuditableEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

}
