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

package sierra.habbohotel.room.items.floor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;



public class FloorItemEngine 
{
	public static ConcurrentLinkedQueue<FloorItem> floorItems(int RoomId)
	{
		ConcurrentLinkedQueue<FloorItem> FloorItems = new ConcurrentLinkedQueue<FloorItem>();

		try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `room_items` WHERE roomid = ? AND is_wall = 0");
			{
				Statement.setInt(1, RoomId);
			}

			ResultSet Row = Statement.executeQuery();

			while (Row.next())
				FloorItems.add(new FloorItem(Row.getInt("id"), Row.getInt("baseid"),  Row.getInt("x"),  Row.getInt("y"), Row.getInt("rotation"), Row.getFloat("height"), Row.getString("extra")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return FloorItems;
	}

	public static FloorItem getFloorItem(int Id)
	{
		try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `room_items` WHERE id = ? AND is_wall = 0");
			{
				Statement.setInt(1, Id);
			}

			ResultSet Row = Statement.executeQuery();

			while (Row.next())
				return new FloorItem(Row.getInt("id"), Row.getInt("baseid"),  Row.getInt("x"),  Row.getInt("y"), Row.getInt("rotation"), Row.getFloat("height"), Row.getString("extra"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
