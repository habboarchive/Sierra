package sierra.messages.incoming.handshake;

import sierra.Sierra;
import sierra.events.Event;
import sierra.events.login.CheckReleaseEvent;
import sierra.messages.IMessage;
import sierra.plugin.listeners.ListenerEvent;

public class CheckRelease extends IMessage {

	public String Release;
	
	@Override
	public void handle() throws Exception {
		
		for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.CHECK_RELEASE))
		{
			CheckReleaseEvent Event = new CheckReleaseEvent(session, Release);

			ListenEvent.Event = Event;
			ListenEvent.Listener.onCheckReleaseEvent(Event);
			
			if (ListenEvent.Event.isCancelled())
				return;
		}
	}
}
