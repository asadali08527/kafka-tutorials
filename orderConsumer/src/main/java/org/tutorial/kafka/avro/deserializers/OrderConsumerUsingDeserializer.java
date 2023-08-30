package org.tutorial.kafka.avro.deserializers;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.tutorial.kafka.avro.Order;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;


public class OrderConsumerUsingDeserializer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("value.deserializer",  KafkaAvroDeserializer.class.getName());
        properties.setProperty("group.id", "OrderGroup");
        properties.setProperty("schema.registry.url", "http://localhost:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Order> kafkaConsumer = new KafkaConsumer<>(properties);

        kafkaConsumer.subscribe(Collections.singleton("OrderAvroTopic"));

        ConsumerRecords<String, Order> orders = kafkaConsumer.poll(Duration.ofSeconds(20));

        for (ConsumerRecord<String, Order> record : orders) {
            System.out.println("Key: " + record.key());
            Order order = record.value();
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Product Name: " + order.getProduct());
            System.out.println("Quantity: " + order.getQuantity());
        }

        kafkaConsumer.close();
    }
}
