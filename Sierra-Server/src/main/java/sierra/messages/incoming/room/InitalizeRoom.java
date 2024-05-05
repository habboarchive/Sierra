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

import sierra.Log;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.PaperComposer;
import sierra.messages.outgoing.room.RoomFullComposer;
import sierra.messages.outgoing.room.RoomInitModelData;
import sierra.messages.outgoing.room.RoomOwnerComposer;
import sierra.messages.outgoing.room.RoomRightsComposer;

public class InitalizeRoom extends IMessage
{
	public int Id;

	@Override
	public void handle()
	{	
		try
		{
			if (session.getRoomUser().getInRoom())
				session.getRoomUser().getRoom().leaveRoom(false, false, session);

			Room Room = RoomEngine.getRoom(this.Id);

			if (Room == null)
				return;


			session.getRoomUser().setInRoom(false); // This is set to true when we finish with sending furniture
			session.getRoomUser().setRoom(Room);

			if (Room.isRoomFull() && !session.getHabbo().hasFuse("fuse_enter_full_rooms"))
			{
				session.sendResponse(new RoomFullComposer());
				session.getRoomUser().getRoom().leaveRoom(true, false, session);
				return;
			}

			if (Room.getUsers().size() == 0)
			{
				Log.writeLine("Room loaded '" + Room.getName() + "' ID: " + Room.getId());
			}

			session.getRoomUser().getRoom().getUsers().add(session);

			if (session.getRoomUser().getIsTeleporting() == false)
			{
				session.getRoomUser().setX(session.getRoomUser().getModel().getDoorX());
				session.getRoomUser().setY(session.getRoomUser().getModel().getDoorY());
				session.getRoomUser().setRotation(session.getRoomUser().getModel().getDoorRot());
				session.getRoomUser().setIsTeleporting(false);
			}
			session.getRoomUser().setHeight(session.getRoomUser().getModel().getDoorZ());

			session.sendResponse(new RoomInitModelData(session.getRoomUser().getModel().getName(), session.getRoomUser().getRoom().getId()));

			if (!Room.getWall().equals("0"))
			{
				session.sendResponse(new PaperComposer("wallpaper", Room.getWall()));
			}

			if (!Room.getFloor().equals("0"))
			{
				session.sendResponse(new PaperComposer("floor", Room.getFloor()));
			}

			session.sendResponse(new PaperComposer("landscape", Room.getLandscape()));

			if (Room.hasRights(true, session))
			{
				session.sendResponse(new RoomRightsComposer(4));
				session.sendResponse(new RoomOwnerComposer());
			}
			else if (Room.hasRights(false, session))
			{
				session.sendResponse(new RoomRightsComposer(1));
			}
			else
			{
				session.sendResponse(new RoomRightsComposer(0));
			}

			session.getResponse().init(Outgoing.RoomEvents);
			session.getResponse().appendString("-1");
			//Session.sendResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
