package sg.com.prudential.esb.serializer;

import java.util.ArrayList;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import sg.com.prudential.esb.model.ESBMessage;

public class ESBMessageHandler implements Runnable {
	private ConsumerRecord<String, ESBMessage> record;
	private ArrayList<ESBMessage> instances;
	private String matchingKey;

	/**
	 * @param record
	 * @param instances
	 */
	public ESBMessageHandler(ConsumerRecord<String, ESBMessage> record, ArrayList<ESBMessage> list) {
		this.record = record;
		this.instances = list;
	}

	/**
	 * @param record
	 * @param instances
	 */
	public ESBMessageHandler(ConsumerRecord<String, ESBMessage> record, ArrayList<ESBMessage> list, String key) {
		this.record = record;
		this.instances = list;
		this.matchingKey = key;
	}

	@Override
	public void run() {
		System.out.println("KafkaRecordHandler :: Key = " + record.key());
		System.out.println("KafkaRecordHandler :: value = " + record.value());
		if (matchingKey != null && !matchingKey.equals("")) {
			if (matchingKey.trim().equals(record.key())) {
				instances.add(record.value());
			}
		} else {
			instances.add(record.value());
		}

	}

}