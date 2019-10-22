package com.prudential.pruservice.vpms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prudential.pruservice.vpms.entity.Agent;
import com.prudential.pruservice.vpms.service.AgentService;


/**
 * 
 */

/**
 * @author tamilarasansundaramoorthy
 *
 */
@RestController
public class ServiceController {

	@Autowired
	private AgentService service;

	@PostMapping(path = "/Agent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Agent> createAgent(@RequestBody final Agent agent) {
		System.out.println("Create Agent" + agent);
		return service.createAgent(agent);
	}

	@PutMapping(path = "/Agent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Agent> updateAgent(@RequestBody final Agent agent) {
		System.out.println("Update Agent");
		return service.updateAgent(agent);
	}

	@DeleteMapping(path = "/Agent/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Agent> deleteAgent(@PathVariable String name) {
		System.out.println("Delete Agent");
		return service.DeleteAgent(name);
	}
	
	@GetMapping(path = "/Agent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Agent> readAllAgents() {
		System.out.println("Delete Agent");
		return service.findAllAgents();
	}

}
