package org.tutorials.kafka.OrderProducer.customSerializer.partitioner;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorials.kafka.OrderProducer.OrderCallback;


public class OrderProducerUsingCustomSerializerAndPartitioner {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.tutorials.kafka.OrderProducer.customSerializer.OrderSerializer");
        properties.setProperty("partitioner.class", CustomPartitioner.class.getName());

        KafkaProducer<String, Order> kafkaProducer = new KafkaProducer<>(properties);

        ProducerRecord<String, Order> record = new ProducerRecord<>("OrderPartitionedTopic", "Asad", new Order("Asad", "IPhone 12 Pro", 12));

        try {
            kafkaProducer.send(record, new OrderCallback());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}
