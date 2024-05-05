package sierra.events.messenger;

import sierra.events.IEvent;
import sierra.habbo.Session;

public class PrivateChatEvent extends IEvent {
	
	private String Message;
	private Session To;
	
	public PrivateChatEvent(Session session, Session to, String message) 
	{
		super(session);
		
		this.Message = message;
		this.To = to;
	}
	
	public String getMessage() {
		return this.Message;
	}
	
	public void setMessage(String message) {
		this.Message = message;
	}
	
	public Session getTo() {
		return this.To;
	}
	
	public void setTo(Session to) {
		this.To = to;
	}
}
