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

package sierra.messages.incoming.groups;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;
import sierra.messages.outgoing.groups.BuyGroupDialogComposer;



public class BuyGroupDialog extends IMessage 
{
	@Override
	public void handle()
	{
		/*
		 * [0][0][0][10]
		 * [0][0][0][9]
		 * [1]LúF
		 * [0]
		 * [Â»] Ownerage - Empty [Â»]
		 * 
		 * [1][1]N¦_[0] [Â»] Ownerage's casino shop [Â»][0][1]N°“[0][Â»] Ownerage - Empty [Â»][1][1]ÈyY[0][Â»] Ownerage - Empty [Â»][1][1]ÈÎf[0]$[Â»] Ownerage's gezellige trade [Â»][1][1]Éâ0[0][7]asdf...[0][1]Éä›[0][11]Credit shop[0][1]ÉæD[0] [Â»] Ownerage's credit shop [Â»][0][2]~Û[0][4]Test[0][0][0][0][5][0][0][0][10][0][0][0][3][0][0][0][4][0][0][0][0][0][0][0][0][0][5][0][0][0][0][0][0][0][0][0][3][0][0][0][0][0][0][11][0][0][0][4][0][0][0][0][0][0][0][0][0][0][0][0]
		 */

		ConcurrentLinkedQueue<Room> userRooms = new ConcurrentLinkedQueue<Room>();

		for (Room Room : RoomEngine.roomMap().values())
			if (Room.getOwnerId() == session.getHabbo().Id && !Room.hasGroup())
				userRooms.add(Room);

		session.sendResponse(new BuyGroupDialogComposer(userRooms));
		
		if (!session.getHabbo().GroupColours)
		{
			session.sendResponse(new GuildElementComposer());
			session.getHabbo().GroupColours = true;
		}
	}
}
