package org.tutorial.kafka.avro.deserializers;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;


public class GenericOrderConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("value.deserializer",  KafkaAvroDeserializer.class.getName());
        properties.setProperty("group.id", "OrderGroup");
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        KafkaConsumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(properties);

        kafkaConsumer.subscribe(Collections.singleton("OrderGenericAvroTopic"));

        ConsumerRecords<String, GenericRecord> orders = kafkaConsumer.poll(Duration.ofSeconds(20));

        for (ConsumerRecord<String, GenericRecord> record : orders) {
            System.out.println("Key: " + record.key());
            GenericRecord order = record.value();
            System.out.println("Customer Name: " + order.get("customerName"));
            System.out.println("Product Name: " + order.get("product"));
            System.out.println("Quantity: " + order.get("quantity"));
        }

        kafkaConsumer.close();
    }
}
