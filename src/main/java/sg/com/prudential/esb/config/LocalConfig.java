package sg.com.prudential.esb.config;

import java.util.HashMap;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import sg.com.prudential.esb.model.ESBMessage;
import sg.com.prudential.esb.serializer.ESBMessageDeserializer;
import sg.com.prudential.esb.serializer.ESBMessageSerializer;

@Configuration
public class LocalConfig {

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServer;

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

	@Value("${kafka.message.enable.auto.commit}")
	private String enable_auto_commit;

	@Value("${kafka.message.auto.offset.reset}")
	private String auto_offset_reset;

	@Value("${kafka.message.auto.commit.interval.ms}")
	private String auto_commit_interval_ms;

	@Value("${kafka.message.session.timeout.ms}")
	private String session_timeout_ms;

	@Value("${kafka.message.cleanup.policy}")
	private String cleanupPolicy;

	@Value("${kafka.message.retention.ms}")
	private String retentionPeriod;

	@Value("${kafka.message.retention.bytes}")
	private String retentionBytes;

	@Value("${kafka.message.numPartitions}")
	private String numPartitions;

	@Value("${kafka.message.replicationFactor}")
	private String replicationFactor;

	@Bean
//	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AdminClient prepareKafkaAdminClient() {
		Properties props = new Properties();
		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
		return AdminClient.create(props);
	}

	@Bean("TopicProperties")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public HashMap<String, String> prepareTopicProperties() {
		HashMap<String, String> topicProp = new HashMap<String, String>();
		topicProp.put("cleanup.policy", cleanupPolicy);
		topicProp.put("retention.ms", retentionPeriod);
		topicProp.put("retention.bytes", retentionBytes);
		return topicProp;
	}

	@Bean("ESBMessageProducer")
	public Producer<String, ESBMessage> prepareMessageProducer() {
		Producer<String, ESBMessage> producer = null;
		try {
			Properties producerProps = new Properties();
			producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
			producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ESBMessageSerializer.class.getName());
			producerProps.put(ProducerConfig.ACKS_CONFIG, messageAcknowledgeCount);
			producerProps.put(ProducerConfig.RETRIES_CONFIG, numRetries);
			producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batch_size);
			producerProps.put(ProducerConfig.LINGER_MS_CONFIG, linger_ms);
			producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, buffer_memory);
			producer = new KafkaProducer<String, ESBMessage>(producerProps);
			return producer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return producer;
	}

	@Bean("ConsumerProperties")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Properties getConsumerProperties() {
		Properties KAFKA_PROPERTIES = new Properties();
		KAFKA_PROPERTIES.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
		KAFKA_PROPERTIES.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		KAFKA_PROPERTIES.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ESBMessageDeserializer.class);
		KAFKA_PROPERTIES.put(ConsumerConfig.GROUP_ID_CONFIG, group_id);
		KAFKA_PROPERTIES.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enable_auto_commit);
		KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, auto_offset_reset);
		KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, auto_commit_interval_ms);
		KAFKA_PROPERTIES.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, session_timeout_ms);
		return KAFKA_PROPERTIES;
	}

	@Bean("ESBMessageConsumer")
	public KafkaConsumer<String, ESBMessage> prepareMessageConsumer() {
		KafkaConsumer<String, ESBMessage> myConsumer = null;
		try {
			myConsumer = new KafkaConsumer<>(getConsumerProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myConsumer;
	}

}
