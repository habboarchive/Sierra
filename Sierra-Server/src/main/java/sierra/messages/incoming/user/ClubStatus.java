package sierra.messages.incoming.user;

import sierra.messages.IMessage;
import sierra.messages.outgoing.user.ClubStatusComposer;

public class ClubStatus extends IMessage {

	@Override
	public void handle() throws Exception {
		
		session.sendResponse(new ClubStatusComposer(session.getSubscription()));
	}
}
