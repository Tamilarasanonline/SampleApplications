/**
 * 
 */
package com.prudential.pruservice.vpms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tamilarasansundaramoorthy
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
public class VPMSApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(VPMSApplication.class, args);
	}
}
