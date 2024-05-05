package sierra.habbohotel.commands;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;

public class DeleteRoom extends CommandExecutor {

	@Override
	public boolean onCommand(Session sender, String label, String[] args, String message) 
	{
		if (!sender.getRoomUser().getRoom().hasRights(true, sender))
		{
			return false;
		}
		else
		{
			Room Room = sender.getRoomUser().getRoom();
			
			for (Session RoomUser : Room.getUsers()) {
				RoomUser.getRoomUser().getRoom().leaveRoom(true, false, RoomUser);
			}
			
			RoomEngine.forceRemoveById(Room.getId());
			Sierra.getStorage().executeQuery("DELETE FROM rooms WHERE id = '" + Room.getId() + "'");
			Sierra.getStorage().executeQuery("DELETE FROM room_items WHERE roomid = '" + Room.getId() + "'");
			
			sender.sendNotify("Your room '" + Room.getName() + "' has been deleted.");
			
			return true;
		}
	}
}
