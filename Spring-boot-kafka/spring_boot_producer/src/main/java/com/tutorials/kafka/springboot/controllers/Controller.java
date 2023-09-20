package com.tutorials.kafka.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorials.kafka.springboot.dto.User;
import com.tutorials.kafka.springboot.services.ProducerService;

@RestController
@RequestMapping("/api")
public class Controller {

	@Autowired
	private ProducerService producerService;

	@PostMapping("/publish/data")
	public void publish(@RequestBody User user) {
		producerService.sendData(user);
	}

}
