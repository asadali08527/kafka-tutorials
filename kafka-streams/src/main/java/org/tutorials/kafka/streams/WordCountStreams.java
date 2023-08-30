package org.tutorials.kafka.streams;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class WordCountStreams {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-dataflow");
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		//Create Topology
		StreamsBuilder builder = new StreamsBuilder();
		//Read from input stream
		KStream<String, String> stream = builder.stream("streams-wordcount-input");

		KGroupedStream<String, String> kGroupedStream = stream.flatMapValues(mapper->Arrays.asList(mapper.toLowerCase().split(" "))).groupBy((key,value)->value);
		KTable<String, Long> countTable = kGroupedStream.count();
		countTable.toStream().to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
		
		Topology topology = builder.build();
		//System.out.println(topology.describe());
		
		//Start and Stop Stream
		KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
	}

}
