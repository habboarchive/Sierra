package sierra.messages.outgoing.groups;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbohotel.guilds.GuildEngine;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;
import sierra.messages.incoming.groups.GuildElementComposer;
import sierra.messages.incoming.groups.ManageGroupComposer;



public class ManageGroup extends IMessage {

	public int GuildId;
	
	@Override
	public void handle() throws Exception {
		
		if (!session.getHabbo().GroupColours)
		{
			session.sendResponse(new GuildElementComposer());
			session.getHabbo().GroupColours = true;
		}
		
		ConcurrentLinkedQueue<Room> userRooms = new ConcurrentLinkedQueue<Room>();
		
		for (Room Room : RoomEngine.roomMap().values())
			if (Room.getOwnerId() == session.getHabbo().Id && !Room.hasGroup())
				userRooms.add(Room);
		
		session.sendResponse(new ManageGroupComposer(GuildEngine.getGroup(GuildId), userRooms));
	}

}
