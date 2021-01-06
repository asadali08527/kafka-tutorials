package com.geekglacier.library.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//@Component
public class LibraryEventsConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {

	private final String TOPIC = "library-events";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	@KafkaListener(topics = { TOPIC })
	//@Payload(required = false)
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
		logger.info("ConsumerRecord={}", consumerRecord);
		acknowledgment.acknowledge();
	}

}
