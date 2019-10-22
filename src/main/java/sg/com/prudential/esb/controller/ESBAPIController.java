/**
 * 
 */
package sg.com.prudential.esb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sg.com.prudential.esb.model.CallBack;
import sg.com.prudential.esb.model.ESBMessage;
import sg.com.prudential.esb.model.EndPoint;
import sg.com.prudential.esb.model.Service;
import sg.com.prudential.esb.repo.ServiceRepository;
import sg.com.prudential.esb.serializer.ESBMessageHandler;
import sg.com.prudential.esb.serializer.MessageRequest;

/**
 * @author tamilarasan.s
 *
 */
@RestController
public class ESBAPIController {

	@Autowired
	private ServiceRepository repo;

	@Autowired
	private AdminClient adminClient;

//	@Autowired
//	private SchemaRegistryService schemaService;

	@Value("${esb.message.topic.default}")
	private String esb_topic_name;

	@Value("${kafka.message.numPartitions}")
	private String numPartitions;

	@Value("${kafka.message.replicationFactor}")
	private String replicationFactor;

	@Autowired
	@Qualifier("TopicProperties")
	public HashMap<String, String> topicProp;

	@Autowired
	@Qualifier("ESBMessageProducer")
	private Producer<String, ESBMessage> esbProducer;

	@Autowired
	@Qualifier("ESBMessageConsumer")
	private KafkaConsumer<String, ESBMessage> esbConsumer;

	@Autowired
	@Qualifier("ConsumerProperties")
	private Properties consumerProperties;

	private ExecutorService executor;

	@PostMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Service createRegisterService(@RequestBody final Service service) {

		Service savedService = repo.save(service);
//		for (EndPoint point : savedService.getEndpoints()) {
//			schemaService.registerSchema(point.getTopicName(), point.getSchema());
//		}
		System.out.println("Registry Completed");
		return savedService;
	}

	@PutMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service updateRegisterService(@RequestBody final Service service) {
		return repo.save(service);
	}

	@PostMapping(path = "/unregisterService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteRegisteredService(@RequestBody final Service service) {
		repo.delete(service);
	}

	@DeleteMapping(path = "/unregisterEndPoint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void unregisterEndPoint(@RequestBody final EndPoint endPoint) {
//		repo.delete(service);
	}

	@GetMapping(path = "/lookupService/{serviceName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service lookupService(@PathVariable String serviceName) {
		List<Service> list = repo.findByName(serviceName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@PostMapping(path = "/publishMessage")
	public MessageRequest publishMessage(@RequestBody final MessageRequest request) {

		try {
			System.out.println("ESBMessage>>" + request);
			String key = UUID.randomUUID().toString();
			ProducerRecord<String, ESBMessage> record = new ProducerRecord<String, ESBMessage>(
					request.getEndPoint().getName(), key, request.getMessage());
			esbProducer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	@Deprecated
	@PostMapping("/subscribeMessage")
	public void subscribeMessage(EndPoint endpoint, CallBack callback) {

	}

	@PostMapping(path = "/receiveMessage/{timeout}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ESBMessage> receiveMessage(@RequestBody final EndPoint endpoint1, @PathVariable String timeout) {
		final ArrayList<ESBMessage> messages = new ArrayList<ESBMessage>();
		try {
			consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
			KafkaConsumer<String, ESBMessage> customConsumer = new KafkaConsumer<String, ESBMessage>(
					consumerProperties);
			long timeoutSec = Long.parseLong(timeout);
			customConsumer.subscribe(Arrays.asList(endpoint1.getName()));
			ExecutorService esbExecutor = new ThreadPoolExecutor(1, 1, timeoutSec, TimeUnit.SECONDS,
					new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
			ArrayList<ESBMessage> list = getESBMessageFromConsumer(customConsumer, esbExecutor, messages);
			clearConsumer(customConsumer, esbExecutor);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}

	private boolean isTopicExist(DescribeTopicsResult result) {

		try {
			Map<String, KafkaFuture<TopicDescription>> values = result.values();
			for (String value : values.keySet()) {
				KafkaFuture<TopicDescription> kafkaFuture = values.get(value);
				TopicDescription topicDescription = kafkaFuture.get();
				if (topicDescription.name() == null)
					return false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Method verify & create ESB Client Topic.
	 */
	@PostConstruct
	public void prepareTopic() {
		try {

			DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(esb_topic_name));
			if (!isTopicExist(result)) {
				try {
					NewTopic newTopic = new NewTopic(esb_topic_name, Integer.parseInt(numPartitions),
							Short.parseShort(replicationFactor));
					newTopic.configs(topicProp);
					adminClient.createTopics(Collections.singleton(newTopic));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		clearConsumer(esbConsumer, executor);
		System.out.println("Spring Container is destroy! Customer clean up");
	}

	public ArrayList<ESBMessage> getESBMessageFromConsumer(final KafkaConsumer<String, ESBMessage> customConsumer,
			ExecutorService esbExecutor, ArrayList<ESBMessage> result) {
		ConsumerRecords<String, ESBMessage> records = customConsumer.poll(100);
		for (final ConsumerRecord<String, ESBMessage> record : records) {
			esbExecutor.submit(new ESBMessageHandler(record, result));
		}
		customConsumer.commitSync();
		return result;
	}

	public void clearConsumer(KafkaConsumer<String, ESBMessage> customConsumer, ExecutorService executor) {
		if (customConsumer != null) {
			customConsumer.close();
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
