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

package sierra.habbohotel.room.models;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;


public class RoomModelEngine
{
	private static Map<String, RoomModel> Models = new HashMap<String, RoomModel>();

	public static Map<String, RoomModel> modelMap()
	{
		return Models;
	}
	public static RoomModel getModelByName(String Name)
	{
		return Models.get(Name);
	}
	public static void load() throws Exception 
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM room_models;").executeQuery();

		while (Row.next())
		{
			Models.put(Row.getString("id"), new RoomModel(Row.getString("id"), Row.getString("heightmap"), Row.getInt("door_x"), Row.getInt("door_y"), Row.getInt("door_z"), Row.getInt("door_dir")));
		}
		
		////Logger.writeLine("Cached [" + Models.size() + "] room models");
	}
}
