/**
 * 
 */
package sg.com.prudential.esb.controller;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sg.com.prudential.esb.model.ESBMessage;
import sg.com.prudential.esb.model.ESBMessageType;
import sg.com.prudential.esb.model.EndPoint;
import sg.com.prudential.esb.model.EndPointType;
import sg.com.prudential.esb.model.Service;
import sg.com.prudential.esb.serializer.ESBMessageSerializer;
import sg.com.prudential.esb.serializer.MessageRequest;

/**
 * @author tamilarasansundaramoorthy
 *
 */
@RestController
public class TestController {

	@Autowired
	private AdminClient adminClient;

	@Autowired
	@Qualifier("TopicProperties")
	public HashMap<String, String> topicProp;

	@Value("${kafka.message.numPartitions}")
	private String numPartitions;

	@Value("${kafka.message.replicationFactor}")
	private String replicationFactor;

	@GetMapping(path = "/createTopic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<ConfigResource, org.apache.kafka.clients.admin.Config> createTopic(
			@RequestParam(name = "topicName", defaultValue = "ESBTP_AgentService__agent") String topicName) {

		try {

			NewTopic newTopic = new NewTopic(topicName, Integer.parseInt(numPartitions),
					Short.parseShort(replicationFactor));
			newTopic.configs(topicProp);
			adminClient.createTopics(Collections.singleton(newTopic));
			ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);

			// get the current topic configuration
			DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(resource));

			Map<ConfigResource, org.apache.kafka.clients.admin.Config> config = describeConfigsResult.all().get();
			System.out.println(config);

//			adminClient.close();
			return config;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/sendMessage")
	@ResponseBody
	public String publishMessage(@RequestBody final String message) {

		try {
			System.out.println("ESBMessage>>" + message);
			Properties producerProps = new Properties();
			producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
			producerProps.put(ProducerConfig.RETRIES_CONFIG, "0");
			producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
			producerProps.put(ProducerConfig.LINGER_MS_CONFIG, "1");
			producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
			String key = UUID.randomUUID().toString();
			Producer<String, String> producer = new KafkaProducer<String, String>(producerProps);
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("Test", key, message);
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@PostMapping(path = "/sendESBMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MessageRequest publishESBMessage(@RequestBody MessageRequest request) {

		try {

			System.out.println("ESBMessage>>" + request);
			Properties producerProps = new Properties();
			producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ESBMessageSerializer.class.getName());
			producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
			producerProps.put(ProducerConfig.RETRIES_CONFIG, "0");
			producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
			producerProps.put(ProducerConfig.LINGER_MS_CONFIG, "1");
			producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
			String key = UUID.randomUUID().toString();
			Producer<String, ESBMessage> producer = new KafkaProducer<String, ESBMessage>(producerProps);
			ProducerRecord<String, ESBMessage> record = new ProducerRecord<String, ESBMessage>("Test", key,
					request.getMessage());
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	@PostMapping(path = "/publishMessage/{key}/{message}")
	public void publishMessage(@PathVariable String key, @PathVariable String message) {

		try {

			Properties producerProps = new Properties();
			producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
			producerProps.put(ProducerConfig.RETRIES_CONFIG, "0");
			producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
			producerProps.put(ProducerConfig.LINGER_MS_CONFIG, "1");
			producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

			Producer<String, String> producer = new KafkaProducer<String, String>(producerProps);
			producer.send(new ProducerRecord<String, String>("Test", key, message));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject) parser.parse(new FileReader(jsonFile));
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
		point.setName("ESBTP_AgentService__agent");

		Date stamp = new Date(System.currentTimeMillis());
		UUID messageId = UUID.randomUUID();
		String correlationId = UUID.randomUUID().toString();// REQUEST ID

		ESBMessage eSBMessage = new ESBMessage(messageId, "ClientApplicationName", stamp, ESBMessageType.REQUEST,
				"TOPIC_NAME", correlationId, "JSON", "", // Time duriation window 1000
				"", "Test Data");
		return new MessageRequest(point, eSBMessage);
	}

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

	@GetMapping(path = "/deleteTopic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, KafkaFuture<Void>> deleteTopic(
			@RequestParam(name = "topicName", defaultValue = "ESBTP_AgentService__Agent") String topicName) {
		Properties props = new Properties();
		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		AdminClient adminClient = AdminClient.create(props);
		DeleteTopicsResult deleteTopics = adminClient.deleteTopics(Collections.singleton(topicName));
		adminClient.close();
		return deleteTopics.values();
	}

	/* ==================================== */
}
