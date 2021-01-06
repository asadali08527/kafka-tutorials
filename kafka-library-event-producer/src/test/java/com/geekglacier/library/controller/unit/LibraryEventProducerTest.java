package com.geekglacier.library.controller.unit;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekglacier.library.domain.Book;
import com.geekglacier.library.domain.LibraryEvent;
import com.geekglacier.library.producer.LibraryEventProducer;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerTest {

	@InjectMocks
	private LibraryEventProducer libraryEventProducer;

	@Mock
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Spy
	private ObjectMapper objectMapper;

	@Test
	void sendLibraryEventOnFailure() throws JsonProcessingException, InterruptedException, ExecutionException {
		// Given
		LibraryEvent libraryEvent = new LibraryEvent(7, new Book(104, "Spring Kafka", "Ali Asad"));
		SettableListenableFuture future = new SettableListenableFuture();
		future.setException(new RuntimeException("Exception while calling Kafka"));
		// when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		// when
		assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEvent(libraryEvent).get());
		// then

	}

	@Test
	void sendLibraryEventOnSuccess() throws JsonProcessingException, InterruptedException, ExecutionException {
		// Given
		LibraryEvent libraryEvent = new LibraryEvent(7, new Book(104, "Spring Kafka", "Ali Asad"));
		SettableListenableFuture future = new SettableListenableFuture();
		ProducerRecord<Integer, String> producerRecord = new ProducerRecord<Integer, String>("library-events",
				libraryEvent.getLibraryEventId(), objectMapper.writeValueAsString(libraryEvent));
		RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("library-events", 1), 1, 1, 342,
				System.currentTimeMillis(), 1, 2);
		SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);
		future.set(sendResult);
		// when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		// when
		assert (libraryEventProducer.sendLibraryEvent(libraryEvent).get().getRecordMetadata().partition() == 1);
		// then

	}

}
