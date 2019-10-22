package sg.com.prudential.esb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;;

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
	@Column(length = 1000)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EndPoint))
			return false;
		EndPoint other = (EndPoint) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EndPoint [name=" + name + ", type=" + type + ", schema=" + schema + "]";
	}
	
}
