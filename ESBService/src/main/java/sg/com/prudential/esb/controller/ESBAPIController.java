/**
 * 
 */
package sg.com.prudential.esb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sg.com.prudential.esb.model.CallBack;
import sg.com.prudential.esb.model.EndPoint;
import sg.com.prudential.esb.model.Message;
import sg.com.prudential.esb.model.Service;
import sg.com.prudential.esb.repo.ServiceRepository;

/**
 * @author tamilarasan.s
 *
 */
@RestController
public class ESBAPIController {

	@Autowired
	private ServiceRepository repo;

	@PostMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Service createRegisterService(@RequestBody final Service service) {
		return repo.save(service);
	}

	@PutMapping(path = "/registerService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service updateRegisterService(@RequestBody final Service service) {
		return repo.save(service);

	}

	@DeleteMapping(path = "/unregisterService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteRegisteredService(@RequestBody final Service service) {
		repo.delete(service);
	}

	@GetMapping(path = "/lookupService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Service lookupService(
			@RequestParam(value = "serviceName", defaultValue = "AgentService") String serviceName) {
		List<Service> list = repo.findByName(serviceName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	@Deprecated
	@PostMapping("/publishMessage")
	public void publishMessage(Message message, EndPoint endpoint) {

	}

	@Deprecated
	@PostMapping("/subscribeMessage")
	public void subscribeMessage(EndPoint endpoint, CallBack callback) {

	}

}
