# Kafka Tutorial Project

This project provides a step-by-step demonstration of using Apache Kafka for message production and consumption, including the use of custom serializers and deserializers.

## Table of Contents

- [Introduction](#introduction)
- [Project Structure](#project-structure)
- [Usage](#usage)
  - [OrderProducer](#orderproducer)
  - [OrderProducerUsingCallback](#orderproducerusingcallback)
  - [OrderSerializer and OrderDeserializer](#orderserializer-and-orderdeserializer)
  - [OrderConsumerUsingDeserializer](#orderconsumerusingdeserializer)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Apache Kafka is a distributed event streaming platform that can be used for building real-time data pipelines and streaming applications. This project serves as a tutorial on how to use Kafka in Java, covering key concepts such as message production and consumption, custom serializers and deserializers, and more.

## Project Structure

The project is structured as follows:

- `org.tutorials.kafka.OrderProducer`: Contains classes related to Kafka message production.
  - `OrderProducer`: Produces messages to a Kafka topic.
  - `OrderCallback`: A custom callback for handling producer results.
  - `OrderProducerUsingCallback`: Produces messages with a callback.

- `org.tutorials.kafka.OrderProducer.customSerializer`: Contains classes for custom serialization.
  - `OrderSerializer`: Serializes `Order` objects to JSON.
  
- `org.tutorials.kafka.orderConsumer.customDeserializer`: Contains classes for custom deserialization.
  - `OrderDeserializer`: Deserializes JSON data into `Order` objects.
  - `OrderConsumerUsingDeserializer`: Consumes and deserializes messages from a Kafka topic.

## Usage

### OrderProducer

The `OrderProducer` class demonstrates how to produce messages to a Kafka topic. It uses default serializers for key and value.

### OrderProducerUsingCallback

The `OrderProducerUsingCallback` class produces messages to a Kafka topic and uses a custom callback (`OrderCallback`) to handle the results of the send operation.

### OrderSerializer and OrderDeserializer

The `OrderSerializer` class serializes `Order` objects to JSON, while the `OrderDeserializer` class deserializes JSON data into `Order` objects. These are used for custom serialization and deserialization.

### OrderConsumerUsingDeserializer

The `OrderConsumerUsingDeserializer` class demonstrates how to consume and deserialize messages from a Kafka topic using a custom deserializer (`OrderDeserializer`) for `Order` objects.

## Contributing

Contributions to this project are welcome. If you find any issues or have suggestions for improvements, please open an issue or create a pull request.


