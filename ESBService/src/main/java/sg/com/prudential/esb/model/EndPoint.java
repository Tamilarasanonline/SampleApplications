package sg.com.prudential.esb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESB_ENDPOINT")
public class EndPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NAME", unique = true, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 8)
	private EndPointType type;

	// JSON Schema
	@Column(length = 50)
	private String schema;

	/**
	 * 
	 */
	public EndPoint() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param name
	 * @param type
	 * @param schema
	 */
	public EndPoint(String name, EndPointType type, String schema) {
		super();
		this.name = name;
		this.type = type;
		this.schema = schema;
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
	 * @return the type
	 */
	public EndPointType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EndPointType type) {
		this.type = type;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EndPoint [name=" + name + ", type=" + type + ", schema=" + schema +  "]";
	}

}
