package org.tutorials.kafka.OrderProducer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * A custom callback implementation to handle the result of a Kafka producer send operation.
 */
public class OrderCallback implements Callback {

    /**
     * This method is called when the send operation to Kafka is completed.
     *
     * @param metadata   The metadata for the record that was sent (contains partition and offset).
     * @param exception  The exception, if any, that occurred during the send operation.
     */
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            // If there was an exception during the send operation, print the stack trace
            exception.printStackTrace();
        } else {
            // If the send operation was successful, print the partition and offset
            System.out.println("Record sent successfully to partition: " + metadata.partition());
            System.out.println("Offset in the partition: " + metadata.offset());
        }
    }
}
