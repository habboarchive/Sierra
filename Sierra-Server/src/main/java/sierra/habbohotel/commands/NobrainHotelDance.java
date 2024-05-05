package sierra.habbohotel.commands;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.outgoing.room.DanceComposer;

public class NobrainHotelDance extends CommandExecutor {

	@Override
	public boolean onCommand(Session sender, String label, String[] args, String message) {
	
		for (Session user : Sierra.getSocketFactory().getSessionManager().getSessions().values())
		{
			user.sendRoom(new DanceComposer(user.getHabbo().Id, 1));
		}
		
		return true;
	}

}
