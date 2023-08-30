package org.tutorials.kafka.simpleConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

/**
 * This class demonstrates how to consume messages from a Kafka topic.
 */
public class SimpleConsumer {
	public static void main(String[] args) {
		// Configure Kafka consumer properties
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");

		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		
		// Create a Kafka consumer instance
		KafkaConsumer<String, Integer> kafkaConsumer = new KafkaConsumer<>(properties);
		List<PartitionInfo> partitionsFor = kafkaConsumer.partitionsFor("SimpleConsumerTopic");
		Collection<TopicPartition> partitions = new ArrayList<>();
		for(PartitionInfo partitionInfo:partitionsFor) {
			partitions.add(new TopicPartition("SimpleConsumerTopic", partitionInfo.partition()));
		}
		kafkaConsumer.assign(partitions);

		// Poll for records from Kafka for a specified duration
		ConsumerRecords<String, Integer> orders = kafkaConsumer.poll(Duration.ofSeconds(20));

		// Process and print the consumed records
		for (ConsumerRecord<String, Integer> order : orders) {
			System.out.println("Key: " + order.key());
			System.out.println("Value: " + order.value());
		}

		// Close the Kafka consumer
		kafkaConsumer.close();
	}
}
