package br.com.foursales.app.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Valid
@Builder
public record CreateUserRequest(
    @NotNull
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, min = 3, message = "O nome deve ter entre 3 e 255 caracteres")
    String name,

    @NotNull
    @NotBlank(message = "O username é obrigatório")
    @Size(max = 255, min = 3, message = "O username deve ter entre 3 e 255 caracteres")
    @Pattern(regexp = "^[\\w]+$", message = "O username deve conter apenas letras, números e underscores, sem espaços ou quebras de linha")
    String username,

    @NotNull
    @Email(message = "O email deve ser um email válido")
    @NotBlank(message = "O email é obrigatório")
    String email,

    @NotNull
    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 50, min = 8, message = "A senha deve ter entre 8 e 50 caracteres")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
    String password
) {}
