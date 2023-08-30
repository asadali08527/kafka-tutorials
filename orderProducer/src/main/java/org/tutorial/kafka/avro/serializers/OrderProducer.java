package org.tutorial.kafka.avro.serializers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorial.kafka.avro.Order;
import org.tutorials.kafka.OrderProducer.OrderCallback;

import io.confluent.kafka.serializers.KafkaAvroSerializer;


public class OrderProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        
        properties.setProperty("key.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        KafkaProducer<String, Order> kafkaProducer = new KafkaProducer<>(properties);
        
        Order order = new Order("Asad", "IPhone 13 Pro", 15);

        ProducerRecord<String, Order> record = new ProducerRecord<>("OrderAvroTopic", order.getCustomerName().toString(), order );

        try {
            kafkaProducer.send(record, new OrderCallback());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}
