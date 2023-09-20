package com.tutorials.kafka.springboot.service;

import org.springframework.kafka.annotation.KafkaListener;
 import org.springframework.stereotype.Service;

import com.tutorials.kafka.springboot.dto.User;

@Service
public class ConsumerService {


	@KafkaListener(topics = {"user-topic"})
	public void consume (User user) {
		System.out.println("User age is : "+user.getAge());
		
		
	}


}
