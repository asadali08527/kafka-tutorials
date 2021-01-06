package com.geekglacier.library.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekglacier.library.entity.LibraryEvent;
import com.geekglacier.library.repository.LibraryEventsRepository;

@Service
public class LibraryEventsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LibraryEventsRepository libraryEventsRepository;

	public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord)
			throws JsonMappingException, JsonProcessingException {
		LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
		logger.info("libraryEvent={}", libraryEvent);
		switch (libraryEvent.getLibraryEventType()) {
		case NEW:
			save(libraryEvent);
			break;
		case UPDATE:
			if (isValid(libraryEvent)) {
				save(libraryEvent);
			} else {
				logger.info("Either Library event id is missing or Invalid event {}", libraryEvent);
			}
			break;
		default:
			logger.info("Invalid Library Event Type");
			break;
		}
	}

	private boolean isValid(LibraryEvent libraryEvent) {
		return libraryEvent.getLibraryEventId() == null ? false
				: libraryEventsRepository.findById(libraryEvent.getLibraryEventId()).isPresent() ? true : false;

	}

	private void save(LibraryEvent libraryEvent) {
		libraryEvent.getBook().setLibraryEvent(libraryEvent);
		libraryEventsRepository.save(libraryEvent);
		logger.info("Succesfully persisted libraryEvent={}", libraryEvent);

	}

}
