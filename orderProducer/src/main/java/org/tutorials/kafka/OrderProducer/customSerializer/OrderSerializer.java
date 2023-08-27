package org.tutorials.kafka.OrderProducer.customSerializer;

import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A custom Kafka serializer for serializing Order objects to JSON.
 */
public class OrderSerializer implements Serializer<Order> {

    /**
     * Serialize an Order object to a byte array.
     *
     * @param topic The topic associated with the data.
     * @param order The Order object to serialize.
     * @return The serialized byte array representing the Order object.
     */
    @Override
    public byte[] serialize(String topic, Order order) {
        byte[] response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Serialize the Order object to JSON and convert it to bytes
            response = objectMapper.writeValueAsString(order).getBytes();
        } catch (JsonProcessingException e) {
            // Handle any serialization errors and print the stack trace
            e.printStackTrace();
        }
        return response;
    }
}
