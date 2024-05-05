package sierra.messages.incoming.shop;

import sierra.messages.IMessage;
import sierra.messages.outgoing.user.ClubPackagesComposer;

public class ClubPackages extends IMessage {

	@Override
	public void handle() {
		session.sendResponse(new ClubPackagesComposer(session.getSubscription()));
	}

}
