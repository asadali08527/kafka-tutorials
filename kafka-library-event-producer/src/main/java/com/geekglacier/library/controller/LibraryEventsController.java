package com.geekglacier.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekglacier.library.domain.LibraryEvent;
import com.geekglacier.library.enums.LibraryEventType;
import com.geekglacier.library.producer.LibraryEventProducer;

@RestController
@RequestMapping("/v1/library/events")
public class LibraryEventsController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LibraryEventProducer libraryEventProducer;

	@PostMapping("/publish")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws Exception {
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		logger.info("Before sendLibraryEvent");
		// libraryEventProducer.sendLibraryEvent(libraryEvent); //Approach 1
		// SendResult<Integer, String> sendResult =
		/*
		 * libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
		 * logger.info("Send result ={} ", sendResult.toString());
		 */
		libraryEventProducer.sendLibraryEventApproach3(libraryEvent);
		logger.info("After sendLibraryEvent");
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}

	@PutMapping("/publish")
	public ResponseEntity<?> putLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws Exception {
		if (libraryEvent.getLibraryEventId() == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kindly pass libraryEventid");

		libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
		logger.info("Before sendLibraryEvent");
		// libraryEventProducer.sendLibraryEvent(libraryEvent); //Approach 1
		// SendResult<Integer, String> sendResult =
		/*
		 * libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
		 * logger.info("Send result ={} ", sendResult.toString());
		 */
		libraryEventProducer.sendLibraryEventApproach3(libraryEvent);
		logger.info("After sendLibraryEvent");
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}

}
