# i2i-Academy-EDA_With_Apache_Kafka-6

This repository contains the implementation of an Event-Driven Architecture (EDA) using Apache Kafka, developed as part of the i2i Academy internship program. The project demonstrates real-time data stream processing by transmitting custom telecom data objects between a local client and an AWS EC2 cloud infrastructure.

## Project Overview

The core purpose of this project is to shift from traditional synchronous Request-Response models to an asynchronous, decoupled Event-Driven Architecture. The system models a telecommunication scenario where Call Detail Records (CDRs) are generated, serialized into JSON format, transmitted through a Kafka broker, and consumed in real time.

The project consists of three main parts:
1. **Local Infrastructure Setup:** Running Apache Kafka and Zookeeper locally using Docker Compose.
2. **Application Development:** Writing Java applications for data generation (Producer) and real-time ingestion (Consumer) with custom object serialization.
3. **Cloud Deployment:** Moving the Kafka ecosystem to an Amazon Web Services (AWS) EC2 virtual machine and conducting remote integration testing.

---

## Architecture & Components

The system architecture utilizes the following components:

* **Event (Data Payload):** Represented by `CdrModel`, which stores core telecom metrics such as unique IDs, phone numbers, and call durations.
* **Producer:** A Java application that generates `CdrModel` objects, converts them into JSON strings using the Jackson library, and publishes them to the Kafka cluster.
* **Kafka Broker:** The central event streaming engine responsible for persisting, ordering, and distributing messages via the `i2i-cdr-topic`.
* **Zookeeper:** Serves as the coordination and configuration manager, overseeing the state and health of the Kafka broker.
* **Consumer:** A continuous Java service running an infinite loop (`while(true)`) that polls the topic, extracts JSON records, deserializes them back into Java objects, and outputs them to the terminal.

---

## Technologies & Tools Used

* **Programming Language:** Java (JDK 21)
* **Stream Processing Platform:** Apache Kafka & Apache Zookeeper (Confluent Platform v7.5.0)
* **Containerization:** Docker & Docker Compose
* **Dependency Management:** Apache Maven
* **JSON Processing Library:** Jackson Databind
* **Cloud Infrastructure:** Amazon Web Services (AWS) EC2 (Ubuntu 24.04 LTS, t3.micro instance)

---

## Technical Challenges & Memory Management

During the cloud deployment phase on the AWS EC2 instance, a critical infrastructure limitation was encountered. The target server was an AWS `t3.micro` instance with only 1 GB of total physical RAM. 

By default, the Apache Kafka broker attempts to reserve a substantial amount of system memory for its Java Virtual Machine (JVM) heap space. Due to these high resource demands, the server ran out of memory, causing the Linux operating system to trigger the Out-Of-Memory (OOM) Killer mechanism, which terminated the Kafka container immediately upon startup.

### Solution:
To overcome this memory constraint, precise memory allocation management was applied. The `docker-compose.yml` file on the cloud environment was modified to restrict Kafka's heap size. By injecting the environment variable `KAFKA_HEAP_OPTS: "-Xmx256m -Xms256m"`, Kafka was forced to operate efficiently within a maximum limit of 256 MB of RAM. This dietic configuration resolved the resource exhaustion issue, allowing both Zookeeper and Kafka to run stably in the cloud.

---

## Repository Structure

The project directory must follow the official Apache Maven standards:

```text
i2i-Academy-EDAwithApacheKafka-6/
│
├── src/
│   └── main/
│       └── java/
│           ├── CdrModel.java       # Custom Java data object (Serializable)
│           ├── CdrProducer.java    # Kafka Producer application (Serializes and sends JSON)
│           └── CdrConsumer.java    # Kafka Consumer application (Listens and deserializes JSON)
│
├── docker-compose.yml              # Local/Cloud Docker configuration file
├── pom.xml                         # Maven project configuration and dependencies
└── README.md                       # Project documentation
