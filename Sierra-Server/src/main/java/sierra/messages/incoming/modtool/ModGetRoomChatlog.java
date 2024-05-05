package sierra.messages.incoming.modtool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomChatlog;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;



public class ModGetRoomChatlog extends IMessage 
{
	private int Junk = 0;
	private int RoomId;

	@Override
	public void handle()
	{
		try
		{
			if (!session.getHabbo().hasFuse("fuse_mod"))
				return;

			Room Room = RoomEngine.getRoom(RoomId);

			if (Room == null)
			{
				return;
			}

			session.getResponse().init(Outgoing.RoomChatlog);
			session.getResponse().appendBoolean(true);
			session.getResponse().appendInt32(Room.getId());
			session.getResponse().appendString(Room.getName());

			PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `room_chatlogs` WHERE `room_id` = ? ORDER by `timestamp` DESC LIMIT 100");
			{
				Statement.setInt(1, RoomId);
			}

			ResultSet Row = Statement.executeQuery();

			List<RoomChatlog> RoomChatlogs = new ArrayList<RoomChatlog>();
			
			while (Row.next())
			{
				RoomChatlog roomChatlog = new RoomChatlog(Row.getString("username"), Row.getString("message"), Row.getInt("hour"), Row.getInt("minute"));
				
				RoomChatlogs.add(roomChatlog);
			}
			
			session.getResponse().appendInt32(RoomChatlogs.size());
			
			for (RoomChatlog RoomChat : RoomChatlogs)
			{
				RoomChat.Serialize(session.getResponse());
			}
			
			//session.sendResponse();
			
			RoomChatlogs.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			Junk = Junk + 1;
		}
	}
}