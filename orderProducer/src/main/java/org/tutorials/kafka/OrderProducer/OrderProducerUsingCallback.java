package org.tutorials.kafka.OrderProducer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This class demonstrates how to produce messages to a Kafka topic using a callback.
 */
public class OrderProducerUsingCallback {

    public static void main(String[] args) {
        // Configure Kafka producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");

        // Create a Kafka producer instance
        KafkaProducer<String, Integer> kafkaProducer = new KafkaProducer<>(properties);

        // Create a producer record with topic, key, and value
        ProducerRecord<String, Integer> record = new ProducerRecord<>("OrderTopic", "Iphone 12 Pro", 12);

        try {
            // Send the record to Kafka with a custom callback for handling the result
            kafkaProducer.send(record, new OrderCallback());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the Kafka producer
            kafkaProducer.close();
        }
    }
}
