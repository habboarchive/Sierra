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

package sierra.habbohotel.room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import sierra.Log;
import sierra.Sierra;



public class RoomEngine
{
	private static ConcurrentMap<Integer, Room> Rooms;

	static {
		Rooms = new ConcurrentHashMap<Integer, Room>();
	}

	public static ConcurrentMap<Integer, Room> roomMap()
	{
		return Rooms;
	}

	public static Room getRoom(int Id)
	{
		
		try {
			return Rooms.containsKey(Id) ? Rooms.get(Id) : cacheSingleRoom(Id);
		} catch (Exception e) {
			return null;
		}
	}

	public static Room getRoom(int OwnerId, String Name)
	{
		for (Room Room : RoomEngine.Rooms.values())
		{
			if (Room.getOwnerId() == OwnerId && Room.getName().equals(Name))
			{
				return Room;
			}
		}
		return null;
	}

	public static Boolean ownerOffline(Room Room)
	{
		if (Sierra.getSocketFactory().getSessionManager().userExists(Room.getOwnerId()) == false)
			return true;
		else
			return false;
	}

	public static ConcurrentLinkedQueue<Room> listRooms(int Id)
	{
		ConcurrentLinkedQueue<Room> Rooms = new ConcurrentLinkedQueue<Room>();

		for (Room Room : RoomEngine.Rooms.values())
		{
			if (Room.getOwnerId() == Id)
			{
				Rooms.add(Room);
			}
		}

		return Rooms;
	}

	public static void removeByOwnerId(int Id)
	{
		for (Room Room : Rooms.values())
		{
			if (Room.getOwnerId() == Id && Room.getUsers().size() == 0)
			{
				disposeRoom(Room);
			}
		}
	}

	public static void removeById(int Id)
	{
		for (Room Room : Rooms.values())
		{
			if (Room.getId() == Id && Room.getUsers().size() == 0 && ownerOffline(Room))
			{
				disposeRoom(Room);
			}
		}
	}
	
	public static void forceRemoveById(int Id)
	{
		for (Room Room : Rooms.values())
		{
			if (Room.getId() == Id)
			{
				disposeRoom(Room);
			}
		}
	}

	public static void disposeRoom(Room room)
	{
		room.getFloorItems().clear();
		room.getWallItems().clear();
		room.getUsers().clear();
		room.getPets().clear();
		room.getRights().clear();
		
		if (room.hasGroup())
		{
			room.getGroup().dispose();
		}
		
		room.RoomActive = false;

		Rooms.remove(room.Id);

		Log.writeLine("Room unloaded '" + room.getName() + "' ID: " + room.getId());
	}

	public static void cacheOwnRooms(int OwnerId) throws Exception
	{
		PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `rooms` WHERE ownerid = ?");
		{
			Statement.setInt(1, OwnerId);
		}

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			int RoomId = Row.getInt("id");
		
			if (!Rooms.containsKey(RoomId))
			{
				String Owner = Sierra.getStorage().readString("SELECT username FROM `members` WHERE id = '" + OwnerId + "'");

				Rooms.put(RoomId, new Room(RoomId).fillData(Row, OwnerId, Owner));
			}
		}
	}

	public static Room cacheSingleRoom(int Id) throws Exception 
	{
		if (!Rooms.containsKey(Id))
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `rooms` WHERE id = ?");
			{
				Statement.setInt(1, Id);
			}

			ResultSet Row = Statement.executeQuery();

			while (Row.next())
			{
				int RoomId = Row.getInt("id");
				int OwnerId = Row.getInt("ownerid");
				String Owner = Sierra.getStorage().readString("SELECT username FROM `members` WHERE id = '" + OwnerId + "'");

				Rooms.put(RoomId, new Room(RoomId).fillData(Row, OwnerId, Owner));
			}
		}

		return Rooms.get(Id);
	}
}
