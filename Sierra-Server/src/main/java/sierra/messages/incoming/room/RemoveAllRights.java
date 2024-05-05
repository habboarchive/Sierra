package sierra.messages.incoming.room;

import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.QuitRights;
import sierra.messages.outgoing.room.RemovePowers;
import sierra.messages.outgoing.room.RoomRightsComposer;



public class RemoveAllRights extends IMessage {

	@Override
	public void handle() throws SQLException {

		for (int UserId : session.getRoomUser().getRoom().getRights())
		{
			Session user = Sierra.getSocketFactory().getSessionManager().getUserById(UserId);

			if (user != null)
			{
				if (user.getHabbo().hasFuse("fuse_any_room_rights"))
				{
					return;
				}

				if (user.getRoomUser().isOwner())
				{
					return;
				}

				if (!session.getRoomUser().getRoom().getRights().contains(UserId))
				{
					return;
				}

				if (user.getRoomUser().getRoom() == session.getRoomUser().getRoom())
				{
					user.getRoomUser().getStatuses().remove("flatctrl 1");
					user.getRoomUser().getStatuses().put("flatctrl 0", "");
					user.getRoomUser().updateStatus();

					user.sendResponse(new RoomRightsComposer(0));
					user.sendResponse(new RemovePowers(session.getRoomUser().getRoom().getId(), UserId));
					user.sendResponse(new QuitRights());
				}

			}

			Sierra.getStorage().executeQuery("DELETE FROM room_rights WHERE roomid = '" + session.getRoomUser().getRoom().getId() + "' AND userid = '" + UserId + "'");
			
			session.getRoomUser().getRoom().getRights().remove(UserId);
		}

	}
}