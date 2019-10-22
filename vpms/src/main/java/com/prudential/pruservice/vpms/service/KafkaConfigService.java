/**
 * 
 */
package com.prudential.pruservice.vpms.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prudential.pruservice.vpms.entity.Agent;
import com.prudential.pruservice.vpms.handler.KafkaRecordHandler;

import sg.com.prudential.esb.model.ESBMessage;
import sg.com.prudential.esb.model.ESBMessageType;
import sg.com.prudential.esb.serializer.ESBMessageDeserializer;
import sg.com.prudential.esb.serializer.ESBMessageSerializer;

/**
 * @author tamilarasansundaramoorthy
 *
 */
@Service
public class KafkaConfigService {

	private Properties KAFKA_PROPERTIES = new Properties();

	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;

	@Value("${kafka.message.acks.count}")
	private String messageAcknowledgeCount;

	@Value("${kafka.message.retries}")
	private String numRetries;

	@Value("${kafka.message.batch.size}")
	private String batch_size;

	@Value("${kafka.message.linger.ms}")
	private String linger_ms;

	@Value("${kafka.message.buffer.memory}")
	private String buffer_memory;

	@Value("${kafka.topic.group.id}")
	private String group_id;

	@Value("${application.registered.topic}")
	private String topicName;

	@Autowired
	private AgentService agentService;

	private KafkaConsumer<String, ESBMessage> myConsumer;
	private ExecutorService executor;

	public void processMessage(String key, ESBMessage message) {

		try {
			System.out.println("[" + key + "] =" + message);
			ESBMessage response = new ESBMessage();
			ObjectMapper mapper = new ObjectMapper();

			response.setId(UUID.randomUUID());
			response.setTimestamp(new Date(System.currentTimeMillis()));

			if (message.getType() == ESBMessageType.REQUEST) {
				response.setType(ESBMessageType.RESPOSE);
				response.setCorrelationId(message.getCorrelationId());
				response.setCreator(group_id);
				response.setContentType("JSON");
			}

			Agent agent = mapper.readValue(message.getPayload(), Agent.class);
			List<Agent> allAgents = agentService.createAgent(agent);
			String payload = mapper.writeValueAsString(allAgents);
			response.setPayload(payload);
			sendMessage(message.getReplyTo(), response);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(String topic, ESBMessage message) {
		try {
			Properties producerProps = new Properties();
			String key = message.getId().toString();
			producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
			producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ESBMessageSerializer.class.getName());
			producerProps.put(ProducerConfig.ACKS_CONFIG, messageAcknowledgeCount);
			producerProps.put(ProducerConfig.RETRIES_CONFIG, numRetries);
			producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batch_size);
			producerProps.put(ProducerConfig.LINGER_MS_CONFIG, linger_ms);
			producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, buffer_memory);
			KafkaProducer<String, ESBMessage> producer = new KafkaProducer<String, ESBMessage>(producerProps);
			ProducerRecord<String, ESBMessage> record = new ProducerRecord<String, ESBMessage>(topic, key, message);
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void initIt() throws Exception {
		KAFKA_PROPERTIES.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		KAFKA_PROPERTIES.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		KAFKA_PROPERTIES.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ESBMessageDeserializer.class);
		KAFKA_PROPERTIES.put(ConsumerConfig.GROUP_ID_CONFIG, group_id);
		KAFKA_PROPERTIES.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		KAFKA_PROPERTIES.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		myConsumer = new KafkaConsumer<>(KAFKA_PROPERTIES);
		myConsumer.subscribe(Arrays.asList(topicName));
		init();
		System.out.println("Init method after properties are set : ");
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		shutdown();
		System.out.println("Spring Container is destroy! Customer clean up");
	}

	public void init() {
		int numberOfThreads = 1;
		// Create a threadpool
		executor = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
		while (true) {
			ConsumerRecords<String, ESBMessage> records = myConsumer.poll(100);
			for (final ConsumerRecord<String, ESBMessage> record : records) {
				executor.submit(new KafkaRecordHandler(record, this));
			}
		}
	}

	public void shutdown() {
		if (myConsumer != null) {
			myConsumer.close();
		}
		if (executor != null) {
			executor.shutdown();
		}
		try {
			if (executor != null && !executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}

}
