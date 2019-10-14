package sg.com.prudential.esb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sg.com.prudential.esb.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, String> {
	List<Service> findByName(String name);

}
