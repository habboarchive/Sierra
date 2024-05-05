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

package sierra.habbohotel.navigator.cats;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.room.RoomEngine;


public class CategoryEngine
{
	private static List<RoomCategory> RoomCategories;

	static {
		RoomCategories = new ArrayList<RoomCategory>();
	}

	public static void load() throws Exception
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM navigator_publicrooms;").executeQuery();

		while (Row.next())
		{
			RoomCategories.add(new RoomCategory(Row.getInt("image_size"), Row.getString("image_link"), Row.getString("label"), Row.getString("description"), RoomEngine.getRoom(Row.getInt("room_id"))));
		}
	}

	
	public static int getRoomSize() {
		return RoomCategories.size();
	}


	public static List<RoomCategory> getRoomCategories() {
		return RoomCategories;
	}
}
