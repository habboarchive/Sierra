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

import sierra.habbohotel.room.Room;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.HeightmapComposer;
import sierra.messages.outgoing.room.RelativeHeightmapComposer;

public class LoadHeightmap extends IMessage
{
	@Override
	public void handle()
	{
		Room Room = session.getRoomUser().getRoom();
		
		if (Room == null)
		{
			session.sendNotify("Unknown Room.");
			session.destroy();
		}
		else
		{
            session.sendResponse(new HeightmapComposer(session.getRoomUser().getModel().getMap()));
            session.sendResponse(new RelativeHeightmapComposer(session.getRoomUser().getModel().getRelativeHeightmap()));
		}
	}
}
