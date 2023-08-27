package org.tutorials.kafka.orderConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * This class demonstrates how to consume messages from a Kafka topic.
 */
public class OrderConsumer {
    public static void main(String[] args) {
        // Configure Kafka consumer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.setProperty("group.id", "OrderGroup");

        // Create a Kafka consumer instance
        KafkaConsumer<String, Integer> kafkaConsumer = new KafkaConsumer<>(properties);

        // Subscribe to the "OrderTopic" Kafka topic
        kafkaConsumer.subscribe(Collections.singleton("OrderTopic"));

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
