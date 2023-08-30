package org.tutorials.kafka.orderConsumer.customDeserializer.partitioner.commit.rebalance;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A custom Kafka deserializer for deserializing JSON data into Order objects.
 */
public class OrderDeserializer implements Deserializer<Order> {

    /**
     * Deserialize a byte array into an Order object.
     *
     * @param topic The topic associated with the data.
     * @param data  The serialized data to deserialize.
     * @return The deserialized Order object or null in case of deserialization errors.
     */
    @Override
    public Order deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = null;
        try {
            // Deserialize the byte array into an Order object using Jackson
            order = objectMapper.readValue(data, Order.class);
        } catch (IOException e) {
            // Handle any deserialization errors and print the stack trace
            e.printStackTrace();
        }
        return order;
    }
}
