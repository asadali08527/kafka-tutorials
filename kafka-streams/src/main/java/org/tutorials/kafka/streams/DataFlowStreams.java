package org.tutorials.kafka.streams;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class DataFlowStreams {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-dataflow");
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		
		//Create Topology
		StreamsBuilder builder = new StreamsBuilder();
		//Read from input stream
		KStream<String, String> stream = builder.stream("streams-dataflow-input");
		//Perform Business or transformational logic
		stream.foreach((key,value)->System.out.println("Key and Value "+key+" "+value));
		//Write to output stream
		//stream.to("streams-dataflow-output");
		//stream.filter((key,value)->value.contains("Country")).to("streams-dataflow-output");
		stream.filter((key,value)->value.contains("Country")).mapValues(value->value.toUpperCase()).to("streams-dataflow-output");

		
		Topology topology = builder.build();
		//System.out.println(topology.describe());
		
		//Start and Stop Stream
		KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
	}

}
