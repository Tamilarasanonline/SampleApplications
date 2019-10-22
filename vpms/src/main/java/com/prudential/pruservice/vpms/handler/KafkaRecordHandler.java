/**
 * 
 */
package com.prudential.pruservice.vpms.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.prudential.pruservice.vpms.service.KafkaConfigService;

import sg.com.prudential.esb.model.ESBMessage;

/**
 * @author tamilarasansundaramoorthy
 *
 */
public class KafkaRecordHandler implements Runnable {
	private ConsumerRecord<String, ESBMessage> record;
	private KafkaConfigService instance;

	public KafkaRecordHandler(ConsumerRecord<String, ESBMessage> record, KafkaConfigService controller) {
		this.record = record;
		this.instance = controller;
	}

	@Override
	public void run() { // this is where further processing happens
		System.out.println("KafkaRecordHandler :: Key = " + record.key());
		System.out.println("KafkaRecordHandler :: value = " + record.value());
		System.out.println("KafkaRecordHandler :: Thread id = " + Thread.currentThread().getId());
		instance.processMessage(record.key(), record.value());
	}

	
}