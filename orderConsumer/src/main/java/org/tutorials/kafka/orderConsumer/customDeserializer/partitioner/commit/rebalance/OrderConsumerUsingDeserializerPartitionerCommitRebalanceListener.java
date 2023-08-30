package org.tutorials.kafka.orderConsumer.customDeserializer.partitioner.commit.rebalance;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class OrderConsumerUsingDeserializerPartitionerCommitRebalanceListener {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", StringDeserializer.class.getName());
		properties.setProperty("value.deserializer", OrderDeserializer.class.getName());
		properties.setProperty("group.id", "OrderGroup");
		// properties.setProperty("enable.auto.commit","false");//By default It is true

		// properties.setProperty("auto.commit.interval.ms", "7000");//Default auto
		// commit time is 5 secs
		properties.setProperty("auto.commit.offset", "false");

		Map<TopicPartition,OffsetAndMetadata> currentOffset = new HashMap<>();
		
		KafkaConsumer<String, Order> kafkaConsumer = new KafkaConsumer<>(properties);
		
		class RebalanceHandler implements ConsumerRebalanceListener{

			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				//Commit last offset processed
				kafkaConsumer.commitSync(currentOffset);
			}

			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				
			}
			
		}

		kafkaConsumer.subscribe(Collections.singleton("OrderPartitionedTopic"), new RebalanceHandler());
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
					
					currentOffset.put(new TopicPartition(record.topic(),record.partition()), new OffsetAndMetadata(record.offset()+1));
					//Custom Commit
					kafkaConsumer.commitAsync(currentOffset, new OffsetCommitCallback() {
						
						@Override
						public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				
			}
		} finally {
			kafkaConsumer.close();
		}
	}
}
