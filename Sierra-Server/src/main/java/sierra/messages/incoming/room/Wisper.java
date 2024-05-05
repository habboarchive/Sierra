package sierra.messages.incoming.room;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.IMessage;

public class Wisper extends IMessage {

	public String Wispered;

	@Override
	public void handle() throws Exception 
	{
		String User = Wispered.split(" ")[0];
		String Message = Wispered.replace(User + " ", "");
		
		//if (ColouredId >= 18) { return; }
		
		session.getResponse().init(Outgoing.Wisper);
		session.getResponse().appendInt32(session.getHabbo().Id);

		if (User.equals(session.getHabbo().Username))
		{
			session.getResponse().appendString("I'm so lonely...");
		}
		else
		{
			session.getResponse().appendString(Message);
		}

		session.getResponse().appendInt32(session.getRoomUser().grabChatEmotion(Message));
		session.getResponse().appendInt32(0);
		session.getResponse().appendInt32(0);
		session.getResponse().appendInt32(-1);

		session.sendResponse(session.getResponse());

		if (!User.equals(session.getHabbo().Username))
		{
			if (Sierra.getSocketFactory().getSessionManager().getUserWithName(User) != null)
			{
				Sierra.getSocketFactory().getSessionManager().getUserWithName(User).sendResponse(session.getResponse());
			}
		}
	}
}
