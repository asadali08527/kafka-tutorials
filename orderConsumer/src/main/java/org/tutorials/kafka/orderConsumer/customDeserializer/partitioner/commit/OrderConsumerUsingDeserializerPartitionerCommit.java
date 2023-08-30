package org.tutorials.kafka.orderConsumer.customDeserializer.partitioner.commit;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class OrderConsumerUsingDeserializerPartitionerCommit {
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
				/**
				 * Commit offsets returned on the last poll() for all the subscribed list of
				 * topics and partitions.
				 * 
				 * This commits offsets only to Kafka. The offsets committed using this API will
				 * be used on the first fetch after every rebalance and also on startup. As
				 * such, if you need to store offsets in anything other than Kafka, this API
				 * should not be used.
				 * 
				 * This is a synchronous commit and will block until either the commit succeeds,
				 * an unrecoverable error is encountered (in which case it is thrown to the
				 * caller), or the timeout specified by default.api.timeout.ms expires (in which
				 * case a org.apache.kafka.common.errors.TimeoutException is thrown to the
				 * caller).
				 * 
				 * 
				 */
				kafkaConsumer.commitSync();
				/**
				 * OR
				 * 
				 * Commit offsets returned on the last poll(Duration) for all the subscribed
				 * list of topics and partition. Same as commitAsync(null)
				 */
				//kafkaConsumer.commitAsync();
				kafkaConsumer.commitAsync(new OffsetCommitCallback() {
					
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		} finally {
			kafkaConsumer.close();
		}
	}
}
