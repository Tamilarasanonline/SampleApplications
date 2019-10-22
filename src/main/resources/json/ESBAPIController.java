/**
 * 
 */
package sg.com.prudential.esb.controller;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sg.com.prudential.esb.model.CallBack;
import sg.com.prudential.esb.model.EndPoint;
import sg.com.prudential.esb.model.EndPointType;
import sg.com.prudential.esb.model.ESBMessage;
import sg.com.prudential.esb.model.ESBMessageType;
import sg.com.prudential.esb.model.Service;
import sg.com.prudential.esb.repo.ServiceRepository;
import sg.com.prudential.esb.serializer.MessageRequest;

/**
 * @author tamilarasan.s
 *
 */
@RestController
public class ESBAPIController {

	@Autowired
	private ServiceRepository repo;

//	@Value("${kafka.message.numPartitions}")
//	private String numPartitions;
//
//	@Value("${kafka.bootstrap.replicationFactor}")
//	private String replicationFactor;
//
//	@Value("${kafka.bootstrap.retention.ms}")
//	private String retentionPeriod;
//
//	@Value("${kafka.topic.cleanup.policy}")
//	private String cleanupPolicy;
//
//	@Value("${kafka.topic.retention.bytes}")
//	private String retentionBytes;
//
//	@Autowired
//	private AdminClient adminClient;
//
//	@Autowired
//	@Qualifier("ProducerProperties")
//	private Properties producerProperties;
//
//	@Autowired
//	@Qualifier("ConsumerProperties")
//	private Properties consumerProperties;

	@GetMapping(path = "/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Service test() {
		Service service = new Service("AgentService");
		ArrayList<EndPoint> endpoints = new ArrayList<EndPoint>();
		endpoints.add(new EndPoint("/agent", EndPointType.QUEUE, "{}"));
		endpoints.add(new EndPoint("/agent/{id}", EndPointType.QUEUE, "{}"));
		service.setEndpoints(endpoints);
		try {
			File jsonFile = new ClassPathResource("json/AgentAvro.json").getFile();
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(new FileReader(jsonFile));
			String jsonValue = obj.toJSONString();
			System.out.println(">>>>>>>>" + jsonFile.exists());
			System.out.println(">><<<>>>>" + jsonValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;// repo.save(service);
	}

	@GetMapping(path = "/createMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MessageRequest createMessage() {

		EndPoint point = new EndPoint("/agent", EndPointType.QUEUE, "{}");
		point.setTopicName("ESBTP_AgentService__agent");

		Date stamp = new Date(System.currentTimeMillis());
		UUID messageId = UUID.randomUUID();
		String correlationId = UUID.randomUUID().toString();// REQUEST ID

		Message message = new Message(messageId, "ClientApplicationName", stamp, MessageType.REQUEST, "TOPIC_NAME",
				correlationId, "JSON", "", // Time duriation window 1000
				"", "Test Data");
		return new MessageRequest(point, message);
	}

//	@GetMapping(path = "/createTopic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public Map<ConfigResource, org.apache.kafka.clients.admin.Config> createTopic(
//			@RequestParam(name = "topicName", defaultValue = "ESBTP_AgentService__agent") String topicName) {
//
//		try {
//
//			NewTopic newTopic = new NewTopic(topicName, Integer.parseInt(numPartitions),
//					Short.parseShort(replicationFactor));
//			HashMap<String, String> topicProp = new HashMap<String, String>();
//			topicProp.put("cleanup.policy", cleanupPolicy);
//			topicProp.put("retention.ms", retentionPeriod);
//			topicProp.put("retention.bytes", retentionBytes);
//			newTopic.configs(topicProp);
//			adminClient.createTopics(Collections.singleton(newTopic));
//			ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
//
//			// get the current topic configuration
//			DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(resource));
//
//			Map<ConfigResource, org.apache.kafka.clients.admin.Config> config = describeConfigsResult.all().get();
//			System.out.println(config);
//
//			adminClient.close();
//			return config;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

//	@GetMapping(path = "/listAllTopics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public Map<String, TopicListing> listAllTopics() {
//
//		try {
//			ListTopicsResult listTopics = adminClient.listTopics();
//			return listTopics.namesToListings().get();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

//	@GetMapping(path = "/deleteTopic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public Map<String, KafkaFuture<Void>> deleteTopic(
//			@RequestParam(name = "topicName", defaultValue = "ESBTP_AgentService__agent") String topicName) {
//		DeleteTopicsResult deleteTopics = adminClient.deleteTopics(Collections.singleton(topicName));
//		return deleteTopics.values();
//	}

	/* ==================================== */

	@PostMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Service createRegisterService(@RequestBody final Service service) {
		// DB Store
		// 
		return repo.save(service);
	}

	@PutMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service updateRegisterService(@RequestBody final Service service) {
		return repo.save(service);

	}

	@DeleteMapping(path = "/unregisterService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteRegisteredService(@RequestBody final Service service) {
		repo.delete(service);
	}

	@GetMapping(path = "/lookupService/{serviceName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service lookupService(@PathVariable String serviceName) {
		List<Service> list = repo.findByName(serviceName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	@PostMapping("/publishMessage")
	public void publishMessage(@RequestBody final MessageRequest request) {

		try {
//			String topic = request.getEndPoint().getTopicName();
//			KafkaProducer<String, Message> messageProducer = new KafkaProducer<>(producerProperties);
//
//			int partition = 0;
//			RecordHeaders headers = new RecordHeaders();
//			ProducerRecord<String, Message> record = new ProducerRecord<String, Message>(topic, partition,
//					System.currentTimeMillis(), request.getMessage().getId().toString(), request.getMessage(), headers);
//			Future<RecordMetadata> send = messageProducer.send(record);
//			RecordMetadata recordMetadata = send.get();
//			System.out.println(">>>>" + recordMetadata);
//			messageProducer.close();
////			FlinkKafkaProducer011<Message> producer = new FlinkKafkaProducer011<>(topic, new MessageSerializer(),
////					producerProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	@PostMapping("/subscribeMessage")
	public void subscribeMessage(EndPoint endpoint, CallBack callback) {

	}

	@PostMapping(path = "/receiveMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Message> receiveMessage(@RequestBody final EndPoint endpoint) {
		ArrayList<Message> messageList = new ArrayList<>();
		try {
//			Properties props = new Properties();
//			String topicName = "ESBTP_AgentService__agent";
//
//			props.put("bootstrap.servers", "localhost:9092");
////			ConsumerConfig.GROUP_ID_CONFIG;
//			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//			props.put("group.id", UUID.randomUUID().toString());
////			props.put("enable.auto.commit", "false");
////			props.put("auto.commit.interval.ms", "1000");
//			props.put("session.timeout.ms", "30000");
////			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
////			props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//			props.put("value.deserializer", "sg.com.prudential.esb.serializer.ESBMessageDeserializer");
//
//			KafkaConsumer<String, Message> consumer = new KafkaConsumer<String, Message>(props);
//
//			// Kafka Consumer subscribes list of topics here.
//			consumer.subscribe(Arrays.asList(topicName));
//			// print the topic name
//			System.out.println("Subscribed to topic " + topicName);
//			consumer.beginningOffsets(Arrays.asList(new TopicPartition(topicName, 0)));
//
//			ConsumerRecords<String, Message> records = consumer.poll(Duration.ofSeconds(100));
//			for (ConsumerRecord<String, Message> record : records) {
//				// print the offset,key and value for the consumer records.
//				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
//				messageList.add(record.value());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageList;
	}
}
