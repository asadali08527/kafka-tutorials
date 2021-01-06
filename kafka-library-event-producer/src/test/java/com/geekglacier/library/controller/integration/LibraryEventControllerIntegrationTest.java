package com.geekglacier.library.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.geekglacier.library.domain.Book;
import com.geekglacier.library.domain.LibraryEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = { "library-events" }, partitions = 3)
@TestPropertySource(properties = { "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}" })
public class LibraryEventControllerIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	private Consumer<Integer, String> consumer;

	@BeforeEach
	void setup() {
		Map<String, Object> configs = new HashMap<String, Object>(
				KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer())
				.createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}

	@AfterEach
	void tearDown() {
		consumer.close();
	}

	@Test
	// @Timeout(5)
	void postLibraryEvent() {

		LibraryEvent libraryEvent = new LibraryEvent(null, new Book(103, "Spring Kafka", "Ali Asad"));
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
		ResponseEntity<LibraryEvent> responseEntity = testRestTemplate.exchange("/v1/library/events/publish",
				HttpMethod.POST, new HttpEntity<LibraryEvent>(libraryEvent, httpHeaders), LibraryEvent.class);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, "library-events");
		String value = singleRecord.value();
		String expected = "{\"libraryEventId\":null,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\":103,\"bookName\":\"Spring Kafka\",\"bookAuthor\":\"Ali Asad\"}}";
		assertEquals(expected, value);
	}
}
