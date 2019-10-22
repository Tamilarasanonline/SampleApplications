/**
 * 
 */
package sg.com.prudential.esb.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.com.prudential.esb.model.ESBMessage;

/**
 * @author tamilarasansundaramoorthy
 *
 */
public class ESBMessageDeserializer  
implements Deserializer<ESBMessage> 
{

	@Override
	public void configure(Map map, boolean b) {

	}

	@Override
	public ESBMessage deserialize(String s, byte[] bytes) {
		ObjectMapper mapper = new ObjectMapper();
		ESBMessage obj = null;
		try {
			obj = (ESBMessage) mapper.readValue(bytes, ESBMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public void close() {

	}
	
}
