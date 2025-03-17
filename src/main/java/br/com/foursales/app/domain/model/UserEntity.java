package br.com.foursales.app.domain.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "users")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@ToString(exclude = "roles", callSuper = true)
public class UserEntity extends UuidIdentifierEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserRoleEntity> roles = new HashSet<>();

  public void setUserName(String userName) {
    this.username = userName.toLowerCase();
  }

  public void setEmail(String email) {
	this.email = email.toLowerCase();
  }
}
