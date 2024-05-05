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
import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.RoomEngine;
import sierra.habbohotel.room.models.RoomModel;
import sierra.habbohotel.room.models.RoomModelEngine;
import sierra.messages.IMessage;



public class SearchRooms extends IMessage
{
	public String SearchTerm;
	
	@Override
	public void handle()
	{
		try
		{
			if (SearchTerm.equals("invalid:"))
			{
				session.getResponse().init(Outgoing.NavigatorFlatList);
				session.getResponse().appendInt32(2);
				session.getResponse().appendString("");
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendBoolean(false);
				session.sendResponse(session.getResponse());
				
				return;
			}

			int UserId = 0;
			int Count = 0;
			
			ResultSet Row = null;

			if (SearchTerm.split(":")[0].equals("owner"))
			{
				PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM members WHERE username = ?");
				{
					Statement.setString(1, SearchTerm.split(":")[1]);
				}
				
				Row = Statement.executeQuery();

				while (Row.next())
					UserId = Row.getInt(1);

				Statement = Sierra.getStorage().queryParams("SELECT * FROM rooms WHERE ownerid = '" + UserId + "'");
				Row = Statement.executeQuery();

				Count = Sierra.getStorage().entryCount("SELECT * FROM rooms WHERE ownerid = '" + UserId + "'");
			}
			else
			{
				PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM rooms WHERE name LIKE ? ");
				{
					Statement.setString(1, SearchTerm + "%");
				}

				Row = Statement.executeQuery();

				PreparedStatement CountStatement = Sierra.getStorage().queryParams("SELECT * FROM rooms WHERE name LIKE ? ");
				{
					CountStatement.setString(1, SearchTerm + "%");
				}

				ResultSet CountSet = CountStatement.executeQuery();

				while (CountSet.next())
					Count++;

			}

			session.getResponse().init(Outgoing.NavigatorFlatList);
			session.getResponse().appendInt32(2);
			session.getResponse().appendString("");
			session.getResponse().appendInt32(Count);

			while(Row.next())
			{
				session.getResponse().appendInt32(Row.getInt("id"));
				session.getResponse().appendString(Row.getString("name"));
				session.getResponse().appendBoolean(true);
				session.getResponse().appendInt32(Row.getInt("ownerid"));
				session.getResponse().appendString(Sierra.getStorage().readString("SELECT username FROM members WHERE id = '" + Row.getInt("ownerid") + "'"));
				session.getResponse().appendInt32(0); //TODO: State/locks
				session.getResponse().appendInt32(RoomEngine.getRoom(Row.getInt("id")) != null ? RoomEngine.getRoom(Row.getInt("id")).getUsers().size() : 0);
				session.getResponse().appendInt32(this.maximumInRoom(Row.getString("model")));
				session.getResponse().appendString(Row.getString("description"));
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0); // can trade?
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendString("");
				
				session.getResponse().appendInt32(3); // tag
				
				session.getResponse().appendString("Sierra");
				session.getResponse().appendString("Quackster");
				session.getResponse().appendString("Development");
				
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);
				session.getResponse().appendBoolean(true);
				session.getResponse().appendBoolean(true);
				session.getResponse().appendInt32(0);
				session.getResponse().appendInt32(0);

			}
			
			session.getResponse().appendInt32(0);
			session.getResponse().appendInt32(0);
			session.getResponse().appendBoolean(false);

			session.sendResponse(session.getResponse());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public int maximumInRoom(String Model)
	{	
		RoomModel model = RoomModelEngine.getModelByName(Model);
		
		return model.getMapSizeX() >= 20 || model.getMapSizeY() >= 20 ? 50 : 25;
	}
}
