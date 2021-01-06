package com.geekglacier.library.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.geekglacier.library.service.LibraryEventsService;

@Component
public class LibraryEventsConsumer {

	private final String TOPIC = "library-events";

	@Autowired
	private LibraryEventsService libraryEventsService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@KafkaListener(topics = { TOPIC })
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord)
			throws JsonMappingException, JsonProcessingException {
		logger.info("ConsumerRecord={}", consumerRecord);
		libraryEventsService.processLibraryEvent(consumerRecord);
	}

}
