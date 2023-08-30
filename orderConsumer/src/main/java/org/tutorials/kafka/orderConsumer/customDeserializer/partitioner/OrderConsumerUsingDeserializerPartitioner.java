package org.tutorials.kafka.orderConsumer.customDeserializer.partitioner;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


public class OrderConsumerUsingDeserializerPartitioner {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", StringDeserializer.class.getName());
		properties.setProperty("value.deserializer", OrderDeserializer.class.getName());
		properties.setProperty("group.id", "OrderGroup");

		KafkaConsumer<String, Order> kafkaConsumer = new KafkaConsumer<>(properties);

		kafkaConsumer.subscribe(Collections.singleton("OrderPartitionedTopic"));
		try {
			while (true) {
				ConsumerRecords<String, Order> orders = kafkaConsumer.poll(Duration.ofSeconds(20));

				for (ConsumerRecord<String, Order> record : orders) {
					System.out.println("Key: " + record.key());
					Order order = record.value();
					System.out.println("Customer Name: " + order.getCustomerName());
					System.out.println("Product Name: " + order.getProduct());
					System.out.println("Quantity: " + order.getQuantity());
					System.out.println("Partition: " + record.partition());

				}
			}
		} finally {
			kafkaConsumer.close();
		}
	}
}
