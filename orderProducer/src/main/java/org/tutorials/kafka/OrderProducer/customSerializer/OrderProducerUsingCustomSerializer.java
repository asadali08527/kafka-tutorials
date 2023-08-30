package org.tutorials.kafka.OrderProducer.customSerializer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorials.kafka.OrderProducer.OrderCallback;
import org.tutorials.kafka.OrderProducer.customSerializer.partitioner.Order;

/**
 * This class demonstrates how to produce messages to a Kafka topic using a custom serializer for Order objects.
 */
public class OrderProducerUsingCustomSerializer {
    public static void main(String[] args) {
        // Configure Kafka producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        
        // Use the custom OrderSerializer for value serialization
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.tutorials.kafka.OrderProducer.customSerializer.OrderSerializer");

        // Create a Kafka producer instance
        KafkaProducer<String, Order> kafkaProducer = new KafkaProducer<>(properties);

        // Create a producer record with the custom Order object
        ProducerRecord<String, Order> record = new ProducerRecord<>("OrderCSTopic", "Iphone 12 Pro", new Order("Asad", "IPhone 12 Pro", 12));

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
