package br.com.foursales.app.domain.model;

import br.com.foursales.app.domain.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "users_roles")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserRoleEntity extends UuidIdentifierEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private RoleEnum role;
}
