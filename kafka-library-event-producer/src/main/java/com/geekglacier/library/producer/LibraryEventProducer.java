package com.geekglacier.library.producer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.ListOffsetsOptions;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekglacier.library.domain.LibraryEvent;

@Component
public class LibraryEventProducer {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String TOPIC = "library-events";

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public ListenableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				hanleSuccess(key, value, result);
			}

			private void hanleSuccess(Integer key, String value, SendResult<Integer, String> result) {
				logger.info("Message sent successfully for the key={} value={} and partition={}", key, value,
						result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}

			private void handleFailure(Integer key, String value, Throwable ex) {
				logger.error("Message didn't sent successfully for the key={} value={} and exception={}", key, value,
						ex.getMessage());
				try {
					throw ex;
				} catch (Throwable e) {
					logger.error("Error in Onfailure:{}", e.getMessage());
				}
			}
		});
		return listenableFuture;

	}

	public SendResult<Integer, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws Exception {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		SendResult<Integer, String> result = null;
		try {
			// result = kafkaTemplate.sendDefault(key, value).get();
			result = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Message didn't sent successfully for the key={} value={} and exception={}", key, value,
					e.getMessage());
			throw e;
		}
		return result;
	}

	public ListenableFuture<SendResult<Integer, String>> sendLibraryEventApproach3(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		ProducerRecord<Integer, String> producerRecord = builProducerRecord(key, value, TOPIC);
		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				hanleSuccess(key, value, result);
			}

			private void hanleSuccess(Integer key, String value, SendResult<Integer, String> result) {
				logger.info("Message sent successfully for the key={} value={} and partition={}", key, value,
						result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}

			private void handleFailure(Integer key, String value, Throwable ex) {
				logger.error("Message didn't sent successfully for the key={} value={} and exception={}", key, value,
						ex.getMessage());
				try {
					throw ex;
				} catch (Throwable e) {
					logger.error("Error in Onfailure:{}", e.getMessage());
				}
			}
		});
		return listenableFuture;

	}

	private ProducerRecord<Integer, String> builProducerRecord(Integer key, String value, String topic) {

		List<Header> recordHeader = Arrays.asList(new RecordHeader("event-source", "scanner".getBytes()));
		return new ProducerRecord<Integer, String>(topic, null, key, value, recordHeader);
	}
}
