/**
 * 
 */
package sg.com.prudential.esb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tamilarasan.s
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("sg.com.prudential.esb")
@EntityScan("sg.com.prudential.esb.model")
public class ESBApiApplication {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ESBApiApplication.class, args);
	}

}
