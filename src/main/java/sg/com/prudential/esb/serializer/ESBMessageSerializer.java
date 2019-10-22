/**
 * 
 */
package sg.com.prudential.esb.serializer;


import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.com.prudential.esb.model.ESBMessage;

/**
 * @author tamilarasansundaramoorthy
 *
 */

public class ESBMessageSerializer implements Serializer<ESBMessage> {

	@Override
	public void configure(Map map, boolean b) {

	}

	@Override
	public void close() {

	}
	@Override
	public byte[] serialize(String topic, ESBMessage data) {
		byte[] retVal = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsString(data).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	return retVal;
	}
	String str="{ \"id\": \"1\", \"name\": \"AgentName1\", \"clientId\": \"ClientId1\", \"type\": \"General\",\"addresses\": [ { \"street\": \"mystreet\", \"city\": \"mycity\", \"state\": \"mystate\", \"country\": \"Germany\", \"zip\": \"zip\" }]}";
}