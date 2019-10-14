/**
 * 
 */
package sg.com.prudential.esb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author tamilarasan.s
 *
 */
@Entity
@Table(name = "ESB_SERVICE")
public class Service implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NAME", unique = true, nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "SERVICE_ENDPOINT", joinColumns = {
			@JoinColumn(name = "SERVICE_NAME", referencedColumnName = "NAME") }, inverseJoinColumns = {
					@JoinColumn(name = "ENDPOINT_NAME", referencedColumnName = "NAME") })
	private List<EndPoint> endpoints;

	/**
	 * 
	 */
	public Service() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public Service(String name) {
		super();
		this.name = name;
	}

	/**
	 * @param name
	 * @param endpoints
	 */
	public Service(String name, List<EndPoint> endpoints) {
		super();
		this.name = name;
		this.endpoints = endpoints;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the endpoints
	 */
	public List<EndPoint> getEndpoints() {
		return endpoints;
	}

	/**
	 * @param endpoints the endpoints to set
	 */
	public void setEndpoints(List<EndPoint> endpoints) {
		this.endpoints = endpoints;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", endpoints=" + endpoints + "]";
	}

}
