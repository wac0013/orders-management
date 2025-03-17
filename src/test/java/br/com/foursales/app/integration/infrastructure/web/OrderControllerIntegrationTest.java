package br.com.foursales.app.integration.infrastructure.web;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import br.com.foursales.app.application.dto.OrderCreateRequest;
import br.com.foursales.app.application.dto.OrderItemCreate;
import br.com.foursales.app.domain.enums.RoleEnum;
import br.com.foursales.app.domain.model.UserEntity;
import br.com.foursales.app.domain.model.UserRoleEntity;
import br.com.foursales.app.domain.document.ProductDocument;
import br.com.foursales.app.domain.repository.ProductRepository;
import br.com.foursales.app.domain.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OrderControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private static String accessToken;
	private static List<ProductDocument> products;

    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository,
		@Autowired ProductRepository productRepository,
		@Autowired PasswordEncoder passwordEncoder,
		@Autowired MockMvc mockMvc) throws Exception {

        var user = UserEntity.builder()
            .username("testuser")
            .password(passwordEncoder.encode("testpass"))
            .email("testuser@email.com")
            .name("Test User")
            .build();
        var userRole = UserRoleEntity.builder()
            .user(user)
            .role(RoleEnum.USER)
            .build();
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        var loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\", \"password\":\"testpass\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        accessToken = JsonPath.parse(loginResponse).read("$.access_token", String.class);
    	products = generateAndSaveProducts(productRepository, 10);
    }

	private static List<ProductDocument> generateAndSaveProducts(ProductRepository productRepository, int quantity) {
        List<ProductDocument> productList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            ProductDocument product = ProductDocument.builder()
                .name("Product " + (i + 1))
                .price(BigDecimal.valueOf(Math.random() * 9998.90 + 0.10))
                .currentStock((long) (Math.random() * 9999))
                .build();
            productList.add(product);
        }

        productRepository.saveAll(productList);
        return productList;
    }

    @Test
    public void whenCreateOrderThenStatusIsCreated() throws Exception {
        var orderItem1 = OrderItemCreate.builder().productId(products.get(0).getId()).amount(1).build();
        var orderItem2 = OrderItemCreate.builder().productId(products.get(1).getId()).amount(6).build();
		var totalComputed = products.get(0).getPrice().multiply(BigDecimal.valueOf(orderItem1.amount()))
			.add(products.get(1).getPrice().multiply(BigDecimal.valueOf(orderItem2.amount())));
        var orderRequest = OrderCreateRequest.builder()
            .discount(BigDecimal.valueOf(10.0))
            .items(Set.of(orderItem1, orderItem2))
            .payments(Set.of())
            .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(totalComputed));
    }

    @Test
    public void whenPayOrderThenStatusIsOk() throws Exception {
        String paymentRequest = "{\"paymentDetails\":\"Sample Payment\"}";
        String orderId = UUID.randomUUID().toString();
        Short installment = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/orders/" + orderId + "/payments/installment/" + installment)
                .contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
                .content(paymentRequest)
                .param("id", orderId)
                .param("installment", installment.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

	@Test
	public void whenCreateOrderWithoutTokenThenStatusIsForbidden() throws Exception {
		String orderRequest = "{\"orderDetails\":\"Sample Order\"}";

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderRequest))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void whenPayOrderWithoutTokenThenStatusIsForbidden() throws Exception {
		String paymentRequest = "{\"paymentDetails\":\"Sample Payment\"}";
		String orderId = UUID.randomUUID().toString();
		Short installment = 1;

		mockMvc.perform(MockMvcRequestBuilders.patch("/v1/orders/" + orderId + "/payments/installment/" + installment)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paymentRequest)
				.param("id", orderId)
				.param("installment", installment.toString()))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
