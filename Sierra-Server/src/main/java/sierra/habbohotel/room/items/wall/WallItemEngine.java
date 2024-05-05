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

package sierra.habbohotel.room.items.wall;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;





public class WallItemEngine
{
	public static ConcurrentLinkedQueue<WallItem> wallItems(int RoomId) 
	{
		ConcurrentLinkedQueue<WallItem> WallItems = new ConcurrentLinkedQueue<WallItem>();

		try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `room_items` WHERE roomid = ? AND is_wall = 1;");
			{
				Statement.setInt(1, RoomId);
			}

			ResultSet Row = Statement.executeQuery();

			while (Row.next())
			{
				WallItems.add(new WallItem(Row.getInt("id"), Row.getInt("baseid"), Row.getString("position"), Row.getString("extra")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return WallItems;
	}
}
