package com.geekglacier.library.controller.unit;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekglacier.library.controller.LibraryEventsController;
import com.geekglacier.library.domain.Book;
import com.geekglacier.library.domain.LibraryEvent;
import com.geekglacier.library.producer.LibraryEventProducer;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryEventProducer libraryEventProducer;

	@Test
	public void postTestEvent() throws JsonProcessingException, Exception {
		// LibraryEvent libraryEvent = new LibraryEvent(null, null);

		LibraryEvent libraryEvent = new LibraryEvent(null, new Book(103, "Spring Kafka", "Ali Asad"));
		// doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
		ResultActions resultActions = mockMvc.perform(post("/v1/library/events/publish")
				.content(new ObjectMapper().writeValueAsString(libraryEvent)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void postTestEvent_4xx() throws JsonProcessingException, Exception {
		// LibraryEvent libraryEvent = new LibraryEvent(null, null);

		LibraryEvent libraryEvent = new LibraryEvent(null, new Book(103, "Spring Kafka", "Ali Asad"));
		// doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
		ResultActions resultActions = mockMvc.perform(post("/v1/library/events/publish")
				.content(new ObjectMapper().writeValueAsString(null)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

}
