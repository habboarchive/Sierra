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

package sierra.messages.incoming.navigator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.CreateRoomComposer;





public class CreateRoom extends IMessage 
{
	public String Name;
	public String Model;

	@Override
	public void handle() throws Exception
	{
		if (RoomEngine.getRoom(session.getHabbo().Id, Name) != null)
		{
			session.sendNotify("You already have a room with that name!");
			return;
		}
		
		int Id = 0;
		PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO rooms (`name`, `ownerid`, `description`, `status`, `password`, `model`, `wallpaper`, `floor`, `outside`)" +
				"VALUES (?, ?, '', 'open', '', ?, '0', '0', '0.0')");

		
		Statement.setString(1, Name);
		Statement.setInt(2, session.getHabbo().Id);
		Statement.setString(3, Model);
		Statement.execute();

		ResultSet Keys = Statement.getGeneratedKeys();

		if (Keys.next())
			Id = Keys.getInt(1);

		RoomEngine.cacheSingleRoom(Id);

		session.sendResponse(new CreateRoomComposer(Id, Name));

	}
}
