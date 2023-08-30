package org.tutorials.kafka.producer.configs;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorials.kafka.OrderProducer.OrderCallback;

/**
 * This class demonstrates how to produce messages to a Kafka topic using a callback.
 */
public class OrderProducerUsingConfig {

    public static void main(String[] args) {
        // Configure Kafka producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");

        properties.setProperty(ProducerConfig.ACKS_CONFIG, "0");//Producers receives the acknowledgment from the broker, if replica's 0, 1 or all have receives the message

        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "512");//By default 256MB
        
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");//Possible values snappy, gzip, lz4
        
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "2");//Retries for two times by default after waiting 100ms
        
        properties.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");//Producer will wait for 1000ms for each retry
        
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "1024");//Memory size need to allocate for the batch in bytes, by default it is 16KB
        
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "200");//Producer Will wait before sending the batch
        
        properties.setProperty(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "");//Time in ms for which a producer will wait for a response from the broker and it will timeout if response doesn't come within this ms. 
        
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");// Write message Exactly once
        
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
