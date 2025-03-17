package br.com.foursales.app.integration.infrastructure.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.JsonPath;

import br.com.foursales.app.application.dto.CreateUserRequest;
import br.com.foursales.app.domain.enums.RoleEnum;
import br.com.foursales.app.domain.model.UserEntity;
import br.com.foursales.app.domain.model.UserRoleEntity;
import br.com.foursales.app.domain.repository.UserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Set;

public class AuthControllerIntegrationTest extends BaseControllerIntegrationTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Setup code if needed
    }

    @Test
    public void whenLoginThenReturnAccessAndRefreshTokens() throws Exception {
		var user = UserEntity.builder()
			.username("testuser")
			.password(passwordEncoder.encode("testpass"))
			.email("teste@email.com")
			.name("testname")
			.build();
		var userRole = UserRoleEntity.builder()
			.user(user)
			.role(RoleEnum.USER)
			.build();
		user.setRoles(Set.of(userRole));
		userRepository.save(user);

        // Then, perform the login
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\", \"password\":\"testpass\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists());
    }

    @Test
    public void whenSignupThenStatusIsCreated() throws Exception {
		var request = CreateUserRequest.builder()
			.name("newuser")
			.email("newemail")
			.username("newuser")
			.password("newpass")
			.build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void whenRefreshTokenThenReturnNewAccessAndRefreshTokens() throws Exception {
        var user = UserEntity.builder()
            .username("refreshtestuser")
            .password(passwordEncoder.encode("refreshtestpass"))
            .email("refreshtest@email.com")
            .name("Refresh Test User")
            .build();
        var userRole = UserRoleEntity.builder()
            .user(user)
            .role(RoleEnum.USER)
            .build();
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        // Perform login to get the token
        var loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"refreshtestuser\", \"password\":\"refreshtestpass\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the refresh token from the login response
        var refreshToken = JsonPath.parse(loginResponse).read("$.refresh_token", String.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/auth/refresh")
                .header("Authorization", "Bearer " + refreshToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists());
    }
}
