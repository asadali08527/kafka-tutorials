package org.tutorials.kafka.OrderProducer;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * This class demonstrates how to produce messages to a Kafka topic.
 */
public class OrderProducer {

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
            // Send the record to Kafka and get the metadata for the sent record
            RecordMetadata recordMetadata = kafkaProducer.send(record).get();

            // Print the partition and offset of the sent record
            System.out.println("Record sent successfully to partition: " + recordMetadata.partition());
            System.out.println("Offset in the partition: " + recordMetadata.offset());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the Kafka producer
            kafkaProducer.close();
        }
    }
}
