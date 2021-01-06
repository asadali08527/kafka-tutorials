package com.geekglacier.library.integration;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekglacier.library.consumer.LibraryEventsConsumer;
import com.geekglacier.library.entity.Book;
import com.geekglacier.library.entity.LibraryEvent;
import com.geekglacier.library.enums.LibraryEventType;
import com.geekglacier.library.repository.LibraryEventsRepository;
import com.geekglacier.library.service.LibraryEventsService;

@SpringBootTest
@EmbeddedKafka(topics = { "library-events" }, partitions = 3)
@TestPropertySource(properties = { "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}" })
public class LibraryEventConsumerIntegrationTest {

	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@SpyBean
	LibraryEventsService libraryEventsService;

	@SpyBean
	LibraryEventsConsumer libraryEventsConsumer;

	@Autowired
	private LibraryEventsRepository libraryEventsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
				.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
		}
	}

	// @Test
	void publishNewLibraryEvent()
			throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
		// Given
		String json = "{\"libraryEventId\":3,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\": 1001, \"bookName\":\"Kafka with Spring\",\"bookAuthor\": \"Asad Ali\""
				+ "	}\"}";
		kafkaTemplate.sendDefault(json).get();
		// When
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await(3, TimeUnit.SECONDS);
		// Then
		// verify(libraryEventsConsumer, times(1)).onMessage((ConsumerRecord<Integer,
		// String>) isA(ConsumerRecord.class));
		// verify(libraryEventsService, times(1))
		// .processLibraryEvent((ConsumerRecord<Integer, String>)
		// isA(ConsumerRecord.class));
		List<LibraryEvent> libraryEvents = (List<LibraryEvent>) libraryEventsRepository.findAll();
		libraryEvents.forEach(e -> {
			assert e != null && e.getLibraryEventId() != null && e.getBook() != null;
			// assertEquals(1001, e.getBook().getBookId());
		});
	}

	@Test
	void publishUpdateLibraryEvent()
			throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
		// Given
		String json = "{\"libraryEventId\":4,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\": 1002, \"bookName\":\"Kafka with Spring\",\"bookAuthor\": \"Asad Ali\"}}";
		LibraryEvent libraryEvent = objectMapper.readValue(json, LibraryEvent.class);
		libraryEvent.getBook().setLibraryEvent(libraryEvent);
		libraryEventsRepository.save(libraryEvent);

		// Update the library Event
		Book book = libraryEvent.getBook();
		book.setBookName("Kafka using Spring 2.x");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
		String updatedJson = objectMapper.writeValueAsString(libraryEvent);
		kafkaTemplate.sendDefault(libraryEvent.getLibraryEventId(), updatedJson).get();

		// When
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await(3, TimeUnit.SECONDS);
		// Then
		verify(libraryEventsConsumer, times(1)).onMessage((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
		verify(libraryEventsService, times(1))
				.processLibraryEvent((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
		Optional<LibraryEvent> libraryEventOptional = libraryEventsRepository.findById(4);
		assert libraryEventOptional.isPresent();
		assertEquals("Kafka using Spring 2.x", libraryEventOptional.get().getBook().getBookName());

	}

}
