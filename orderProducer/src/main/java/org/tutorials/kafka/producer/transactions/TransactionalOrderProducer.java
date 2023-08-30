package org.tutorials.kafka.producer.transactions;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorials.kafka.OrderProducer.OrderCallback;

public class TransactionalOrderProducer {

	public static void main(String[] args) {
		// Configure Kafka producer properties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.IntegerSerializer");

		properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "order-producer-1");

		// properties.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG,"1000");
		// Create a Kafka producer instance
		KafkaProducer<String, Integer> kafkaProducer = new KafkaProducer<>(properties);

		/**
		 * Initialize the transaction
		 * 
		 * Needs to be called before any other methods when the transactional.id is set
		 * in the configuration. This method does the following: 1. Ensures any
		 * transactions initiated by previous instances of the producer with the same
		 * transactional.id are completed. If the previous instance had failed with a
		 * transaction in progress, it will be aborted. If the last transaction had
		 * begun completion, but not yet finished, this method awaits its completion. 2.
		 * Gets the internal producer id and epoch, used in all future transactional
		 * messages issued by the producer. Note that this method will raise
		 * TimeoutException if the transactional state cannot be initialized before
		 * expiration of max.block.ms. Additionally, it will raise InterruptException if
		 * interrupted. It is safe to retry in either case, but once the transactional
		 * state has been successfully initialized, this method should no longer be
		 * used.
		 * 
		 */

		kafkaProducer.initTransactions();

		// Create a multiple record with topic, key, and value
		ProducerRecord<String, Integer> record1 = new ProducerRecord<>("OrderTopic", "Iphone 12 Pro", 12);

		ProducerRecord<String, Integer> record2 = new ProducerRecord<>("OrderTopic", "Iphone 13 Pro", 13);

		try {
			/**
			 * Should be called before the start of each new transaction. Note that prior to
			 * the first invocation of this method, you must invoke initTransactions()
			 * exactly one time.
			 */
			kafkaProducer.beginTransaction();
			kafkaProducer.send(record1, new OrderCallback());
			kafkaProducer.send(record2);
			/**
			 * Commits the ongoing transaction. This method will flush any unsent records
			 * before actually committing the transaction. Further, if any of the
			 * send(ProducerRecord) calls which were part of the transaction hit
			 * irrecoverable errors, this method will throw the last received exception
			 * immediately and the transaction will not be committed. So all
			 * send(ProducerRecord) calls in a transaction must succeed in order for this
			 * method to succeed. Note that this method will raise TimeoutException if the
			 * transaction cannot be committed before expiration of max.block.ms.
			 * Additionally, it will raise InterruptException if interrupted. It is safe to
			 * retry in either case, but it is not possible to attempt a different operation
			 * (such as abortTransaction) since the commit may already be in the progress of
			 * completing. If not retrying, the only option is to close the producer.
			 * 
			 */
			kafkaProducer.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * Aborts the ongoing transaction. Any unflushed produce messages will be
			 * aborted when this call is made. This call will throw an exception immediately
			 * if any prior send(ProducerRecord) calls failed with a ProducerFencedException
			 * or an instance of org.apache.kafka.common.errors.AuthorizationException. Note
			 * that this method will raise TimeoutException if the transaction cannot be
			 * aborted before expiration of max.block.ms. Additionally, it will raise
			 * InterruptException if interrupted. It is safe to retry in either case, but it
			 * is not possible to attempt a different operation (such as commitTransaction)
			 * since the abort may already be in the progress of completing. If not
			 * retrying, the only option is to close the producer.
			 * 
			 */
			kafkaProducer.abortTransaction();
		} finally {
			// Close the Kafka producer
			kafkaProducer.close();
		}
	}
}
