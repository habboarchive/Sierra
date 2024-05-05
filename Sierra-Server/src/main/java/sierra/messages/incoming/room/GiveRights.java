package sierra.messages.incoming.room;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.GivePowers;
import sierra.messages.outgoing.room.RoomRightsComposer;



public class GiveRights extends IMessage {

	public int UserId;
	
	@Override
	public void handle() throws SQLException {
		
		Session user = Sierra.getSocketFactory().getSessionManager().getUserById(this.UserId);
		
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
		
		if (session.getRoomUser().getRoom().getRights().contains(user.getHabbo().Id))
		{
			return;
		}
		
		session.getRoomUser().getRoom().getRights().add(user.getHabbo().Id);
		
		user.getRoomUser().getStatuses().remove("flatctrl 0");
		user.getRoomUser().getStatuses().put("flatctrl 1", "");
		user.getRoomUser().updateStatus();
		
		PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO room_rights (`roomid`, `userid`) VALUES (?, ?);");
		{
			Statement.setInt(1, user.getRoomUser().getRoom().getId());
			Statement.setInt(2, user.getHabbo().Id);
			Statement.executeUpdate();
		}
		
		user.sendResponse(new GivePowers(user.getRoomUser().getRoom(), user.getHabbo().Id, user.getHabbo().Username));
		user.sendResponse(new RoomRightsComposer(1));
	}

}
