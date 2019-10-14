/**
 * 
 */
package sg.com.prudential.esb.model;

import java.awt.TrayIcon.MessageType;
import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;

/**
 * @author tamilarasan.s
 *
 */
public class Message {

	// unique Id of the message within a message bus
	private UUID id;
	// client component which generated this message
	private String creator;
	// time when was message was published
	private Date timestamp;
	private MessageType type;
	// Name of topic where response to this request should be sen
	private String replyTo;
	// Id used to correlate request with response
	private String correlationId;
	// the type of message payload. Default application/json
	private String contentType;

	// time duration window (timestamp + ttl) when this message is considered valid
	private String ttl;
	// size of the payload
	private String size;
	private JSONObject payload;

	/**
	 * 
	 */
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param creator
	 * @param timestamp
	 * @param type
	 * @param replyTo
	 * @param correlationId
	 * @param contentType
	 * @param ttl
	 * @param size
	 * @param payload
	 */
	public Message(UUID id, String creator, Date timestamp, MessageType type, String replyTo, String correlationId,
			String contentType, String ttl, String size, JSONObject payload) {
		super();
		this.id = id;
		this.creator = creator;
		this.timestamp = timestamp;
		this.type = type;
		this.replyTo = replyTo;
		this.correlationId = correlationId;
		this.contentType = contentType;
		this.ttl = ttl;
		this.size = size;
		this.payload = payload;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * @return the replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * @return the correlationId
	 */
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * @param correlationId the correlationId to set
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the ttl
	 */
	public String getTtl() {
		return ttl;
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the payload
	 */
	public JSONObject getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(JSONObject payload) {
		this.payload = payload;
	}

}
