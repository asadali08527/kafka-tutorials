package com.tutorials.kafka.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tutorials.kafka.springboot.dto.User;

@Service
public class ProducerService {

	@Autowired
	private KafkaTemplate<String, User> kafkaTemplate;

	public void sendData(User user) {
		kafkaTemplate.send("user-topic", user.getName(), user);
	}

}
