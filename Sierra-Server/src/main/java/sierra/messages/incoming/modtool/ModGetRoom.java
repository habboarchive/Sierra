package sierra.messages.incoming.modtool;

import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;



public class ModGetRoom extends IMessage 
{
	private int RoomId;

	@Override
	public void handle()
	{
		if (!session.getHabbo().hasFuse("fuse_mod"))
			return;
		
		Room Room = RoomEngine.getRoom(RoomId);
		
		List<String> TempTags = new ArrayList<String>();
		
		TempTags.add("Quackster");
		TempTags.add("Sierra");
		TempTags.add("Development");
		
		// TODO: Tags
		
		session.getResponse().init(Outgoing.RoomTool);
		session.getResponse().appendInt32(Room.getId());
		session.getResponse().appendInt32(Room.getUsers().size());
		session.getResponse().appendBoolean(Room.getUsers().contains(Sierra.getSocketFactory().getSessionManager().getUserById(Room.getOwnerId())));
		session.getResponse().appendInt32(Room.getOwnerId());
		session.getResponse().appendString(Room.getOwnerName());
		session.getResponse().appendString(Room.getName());
		session.getResponse().appendString(Room.getDescription());
		session.getResponse().appendInt32(TempTags.size());
		
		for (String tag : TempTags)
			session.getResponse().appendString(tag);
		
		// TODO: Events
		session.getResponse().appendBoolean(false); // TODO: Room has event.
		
		//session.sendResponse();
	}
}
