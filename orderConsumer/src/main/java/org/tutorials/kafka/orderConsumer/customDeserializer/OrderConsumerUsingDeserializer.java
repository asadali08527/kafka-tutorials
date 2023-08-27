package org.tutorials.kafka.orderConsumer.customDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * This class demonstrates how to consume and deserialize messages from a Kafka topic using a custom deserializer for Order objects.
 */
public class OrderConsumerUsingDeserializer {
    public static void main(String[] args) {
        // Configure Kafka consumer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", OrderDeserializer.class.getName());
        properties.setProperty("group.id", "OrderGroup");

        // Create a Kafka consumer instance
        KafkaConsumer<String, Order> kafkaConsumer = new KafkaConsumer<>(properties);

        // Subscribe to the "OrderCSTopic" Kafka topic
        kafkaConsumer.subscribe(Collections.singleton("OrderCSTopic"));

        // Poll for records from Kafka for a specified duration
        ConsumerRecords<String, Order> orders = kafkaConsumer.poll(Duration.ofSeconds(20));

        // Process and print the consumed records
        for (ConsumerRecord<String, Order> record : orders) {
            System.out.println("Key: " + record.key());
            Order order = record.value();
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Product Name: " + order.getProduct());
            System.out.println("Quantity: " + order.getQuantity());
        }

        // Close the Kafka consumer
        kafkaConsumer.close();
    }
}
