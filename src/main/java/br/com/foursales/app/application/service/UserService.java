package br.com.foursales.app.application.service;

import java.text.MessageFormat;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.foursales.app.application.dto.CreateUserRequest;
import br.com.foursales.app.domain.enums.RoleEnum;
import br.com.foursales.app.domain.model.UserEntity;
import br.com.foursales.app.domain.model.UserRoleEntity;
import br.com.foursales.app.domain.repository.UserRepository;
import br.com.foursales.app.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final ModelMapper mapper;
  private final PasswordEncoder passwordEncoder;

  	@Transactional
  	public UserEntity create(CreateUserRequest dto) {
    var user = mapper.map(dto, UserEntity.class);
	var defaultRoleUser = UserRoleEntity.builder()
		.user(user)
		.role(RoleEnum.USER)
		.build();
	user.getRoles().add(defaultRoleUser);
	user.setPassword(passwordEncoder.encode(user.getPassword()));
    return repository.save(user);
  }

  	private boolean isValidEmail(String email) {
      String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
      return email.matches(emailRegex);
  }

	public UserEntity loadUserByUsername(String nameOrEmail) {
		return isValidEmail(nameOrEmail)
		? repository.findByEmail(nameOrEmail)
			.orElseThrow(() -> new NotFoundException("usuario com email " + nameOrEmail + " não encontrado"))
		: repository.findByUsername(nameOrEmail)
			.orElseThrow(() -> new NotFoundException("usuario com nome " + nameOrEmail + " não encontrado"));
	}

	public UserEntity findByIdOrThrow(UUID id) {
		return repository.findById(id)
			.orElseThrow(() -> new NotFoundException(MessageFormat.format("Usuario com id {0} não encontrado", id)));
	}
}
