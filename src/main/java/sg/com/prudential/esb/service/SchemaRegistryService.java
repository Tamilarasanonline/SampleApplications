/**
 * 
 */
package sg.com.prudential.esb.service;

import org.apache.avro.Schema;
import org.springframework.stereotype.Service;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;

/**
 * @author tamilarasansundaramoorthy
 *
 */
//@Service
public class SchemaRegistryService {

	
//	public void registerSchema(String topic, String schema) {
//		try {
//			String url = "http://localhost:18081";
//			String subject = topic + "-value";
//			Schema avroSchema = new Schema.Parser().parse(schema);
//			CachedSchemaRegistryClient client = new CachedSchemaRegistryClient(url, 20);
//			client.updateCompatibility(subject, "NONE");
//			client.register(subject, avroSchema);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//			
//	}
}
