package br.com.foursales.app.integration.infrastructure.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseControllerIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	// @Container
	// private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
	// 	.withDatabaseName("test_db")
	// 	.withUsername("user")
	// 	.withPassword("password");

	// @Container
	// private static final ElasticsearchContainer elasticsearchContainer =
	// 	new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
	// 		.withEnv("discovery.type", "single-node");

	// @Container
	// private static final KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka-native:3.8.0")
	// ;


    // @DynamicPropertySource
    // static void dataSourceConfig(DynamicPropertyRegistry registry) {
    //     registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    //     registry.add("spring.datasource.username", mysqlContainer::getUsername);
    //     registry.add("spring.datasource.password", mysqlContainer::getPassword);
	// 	registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
	// 	registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    // }
}
