package org.tutorial.kafka.avro.serializers;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.tutorial.kafka.avro.Order;
import org.tutorials.kafka.OrderProducer.OrderCallback;

import io.confluent.kafka.serializers.KafkaAvroSerializer;


public class GenericOrderProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        
        properties.setProperty("key.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        KafkaProducer<String, GenericRecord> kafkaProducer = new KafkaProducer<>(properties);
        Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n"
        		+ "\"namespace\": \"org.tutorial.kafka.avro\",\n"
        		+ "\"type\": \"record\",\n"
        		+ "\"name\": \"Order\",\n"
        		+ "\"fields\": [\n"
        		+ "{\n"
        		+ "\"name\": \"customerName\",\"type\":\"string\"\n"
        		+ "},\n"
        		+ "{\n"
        		+ "\"name\": \"product\",\"type\":\"string\"\n"
        		+ "},\n"
        		+ "{\n"
        		+ "\"name\": \"quantity\",\"type\":\"int\"\n"
        		+ "}\n"
        		+ "]\n"
        		+ "}");
        Record order = new GenericData.Record(schema);
        order.put("customerName","Asad");
        order.put("product", "IPhone 14 Pro");
        order.put("quantity", 15);

        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("OrderGenericAvroTopic", order.get("customerName").toString(), order );

        try {
            kafkaProducer.send(record, new OrderCallback());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}
