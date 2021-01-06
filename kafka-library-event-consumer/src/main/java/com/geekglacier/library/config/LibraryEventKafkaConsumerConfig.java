package com.geekglacier.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableKafka
public class LibraryEventKafkaConsumerConfig {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer concurrentKafkaListenerContainerFactoryConfigurer,
			ConsumerFactory<Object, Object> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactoryConfigurer.configure(factory, consumerFactory);
		factory.setConcurrency(3);
		// factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		factory.setErrorHandler((thrownException, data) -> {
			logger.info("Exception in consumer config={} and the record is={}", thrownException.getMessage(), data);
		});
		factory.setRetryTemplate(retryTemplate());
		return factory;
	}

	private RetryTemplate retryTemplate() {
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1000);
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy());
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}

	private RetryPolicy retryPolicy() {
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(3);
		return simpleRetryPolicy;
	}
}
