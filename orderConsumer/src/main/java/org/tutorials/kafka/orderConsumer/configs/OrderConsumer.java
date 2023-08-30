package org.tutorials.kafka.orderConsumer.configs;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.RangeAssignor;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;

/**
 * This class demonstrates how to consume messages from a Kafka topic.
 */
public class OrderConsumer {
    public static void main(String[] args) {
        // Configure Kafka consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "OrderGroup");
        
        properties.setProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1024");//Wait until min set bytes has been received, By default it is 1MB

        properties.setProperty(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "1000");//By default it is 500ms
        
        properties.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "1000");
        
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "3000");
        
        properties.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "5MB");//Default 1MB
        
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");//possible values latest, earliest
        
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "order-consumer-1");// Any unique value
        
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());//Possible values RangeAssignor, RoundRobinAssignor, default is Range 
        		
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
