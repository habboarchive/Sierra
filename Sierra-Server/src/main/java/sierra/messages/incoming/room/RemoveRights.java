package sierra.messages.incoming.room;

import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.QuitRights;
import sierra.messages.outgoing.room.RemovePowers;
import sierra.messages.outgoing.room.RoomRightsComposer;



public class RemoveRights extends IMessage {

	public int Amount;

	@Override
	public void handle() throws SQLException {

		for (int i = 0; i < Amount; i++)
		{
			int UserId = this.request.readInt();
			
			Session user = Sierra.getSocketFactory().getSessionManager().getUserById(UserId);

			if (user == null)
			{
				return;
			}

			if (!user.getRoomUser().getInRoom())
			{
				return;
			}

			if (user.getRoomUser().getRoom() != session.getRoomUser().getRoom())
			{
				return;
			}

			if (user.getHabbo().hasFuse("fuse_any_room_rights"))
			{
				return;
			}

			if (user.getRoomUser().isOwner())
			{
				return;
			}

			if (!session.getRoomUser().getRoom().getRights().contains(user.getHabbo().Id))
			{
				return;
			}

			session.getRoomUser().getRoom().getRights().remove(user.getHabbo().Id);

			user.getRoomUser().getStatuses().remove("flatctrl 1");
			user.getRoomUser().getStatuses().put("flatctrl 0", "");
			user.getRoomUser().updateStatus();

			Sierra.getStorage().executeQuery("DELETE FROM room_rights WHERE roomid = '" + session.getRoomUser().getRoom().getId() + "' AND userid = '" + UserId + "'");

			user.sendResponse(new RoomRightsComposer(0));
			user.sendResponse(new RemovePowers(session.getRoomUser().getRoom().getId(), UserId));
			user.sendResponse(new QuitRights());
		}
	}

}
