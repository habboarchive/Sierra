/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>. 
 * 
 * This file is part of Sierra.
 * 
 * Sierra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sierra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sierra.  If not, see <http ://www.gnu.org/licenses/>.
 */

package sierra.messages.incoming.room;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;

public class DeleteRoom extends IMessage {

	public int Id;

	@Override
	public void handle() {
		
		if (!session.getRoomUser().getRoom().hasRights(true, session)) 
		{
			return;
		}
		
		Sierra.getStorage().executeQuery("DELETE FROM rooms WHERE id = '" + this.Id + "'");
		Sierra.getStorage().executeQuery("DELETE FROM room_items WHERE roomid = '" + this.Id + "'");

		for (Session user : session.getRoomUser().getRoom().getUsers())
		{
			session.getRoomUser().getRoom().leaveRoom(true, false, user);
		}
		
		RoomEngine.forceRemoveById(Id);
	}
}
