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

import sierra.messages.IMessage;
import sierra.messages.outgoing.room.RoomDataComposer;
import sierra.messages.outgoing.room.RoomFloorItemsComposer;
import sierra.messages.outgoing.room.RoomPanelComposer;
import sierra.messages.outgoing.room.RoomWallItemsComposer;

public class RoomExternalData extends IMessage 
{
	@Override
	public void handle()
	{		
		session.sendResponse(new RoomFloorItemsComposer(session.getRoomUser().getRoom()));
		session.sendResponse(new RoomWallItemsComposer(session.getRoomUser().getRoom()));

        if (session.getRoomUser().getRoom().hasRights(false, session))
        {
        	session.getRoomUser().getStatuses().put("flatctrl 1", "");
        }
        else
        {
        	session.getRoomUser().getStatuses().put("flatctrl 0", "");
        }
		
		session.getRoomUser().getRoom().sendUserFigures(session);
		session.getRoomUser().getRoom().sendUserPositions(session);

		session.sendResponse(new RoomPanelComposer(session.getRoomUser().getRoom().getId()));
		session.sendResponse(new RoomDataComposer(session.getRoomUser().getRoom()));
        
        session.getRoomUser().setInRoom(true);
        
        session.getMessenger().sendStatus(true, true);
	}

}
