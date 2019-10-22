package sg.com.prudential.esb.serializer;

import sg.com.prudential.esb.model.EndPoint;
import sg.com.prudential.esb.model.ESBMessage;

public class MessageRequest {
	
	private EndPoint endPoint;
	private ESBMessage eSBMessage;
	
	public MessageRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageRequest(EndPoint endPoint, ESBMessage eSBMessage) {
		super();
		this.endPoint = endPoint;
		this.eSBMessage = eSBMessage;
	}
	public EndPoint getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(EndPoint endPoint) {
		this.endPoint = endPoint;
	}
	public ESBMessage getMessage() {
		return eSBMessage;
	}
	public void setMessage(ESBMessage eSBMessage) {
		this.eSBMessage = eSBMessage;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageRequest [endPoint=" + endPoint + ", eSBMessage=" + eSBMessage + "]";
	}
	
	

}
