/**
 * 
 */
package com.prudential.pruservice.vpms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prudential.pruservice.vpms.entity.Agent;

/**
 * @author tamilarasansundaramoorthy
 *
 */
@Service
public class AgentService {

	private HashMap<String, Agent> inMemoryMap = new HashMap<>();

	public List<Agent> createAgent(Agent input) {
		inMemoryMap.put(input.getId(), input);
		return findAllAgents();
	}

	public List<Agent> updateAgent(Agent input) {
		inMemoryMap.put(input.getId(), input);
		return findAllAgents();
	}

	public List<Agent> findAllAgents() {
		ArrayList<Agent> list = new ArrayList<>();
		for (String agent : inMemoryMap.keySet()) {
			list.add(inMemoryMap.get(agent));
		}
		return list;
	}

	public List<Agent> DeleteAgent(String name) {
		List<Agent> list = new ArrayList<Agent>();
		if (inMemoryMap.containsKey(name)) {
			list.add(inMemoryMap.get(name));
			inMemoryMap.remove(name);
		}
		return findAllAgents();
	}

}
