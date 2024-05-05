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

import java.sql.SQLException;

import sierra.habbohotel.HabboHotel;
import sierra.habbohotel.commands.CommandEngine;
import sierra.messages.IMessage;



public class Shout extends IMessage 
{
	public String Message;
	public int ColourId;

	@Override
	public void handle() throws SQLException
	{
		if (ColourId >= HabboHotel.ColourChatCrash) 
		{ 
			return; 
		}
		
		if (ColourId == 1 || ColourId == 2) 
		{ 
			return; 
		}
		
		if (ColourId == 23 && !session.getHabbo().hasFuse("fuse_mod"))
		{
			return;
		}
		
		if (!session.getRoomUser().getInRoom())
		{
			return;
		}
		
		if (this.Message.startsWith(" "))
		{
			return;
		}
		
		if (Message.startsWith(":"))
		{
			String Label = Message.split(" ")[0].replace(":", "");
			String Command = "";
			
			if (Message.substring(Label.length() + 1).length() > 1)
			{
				Command = Message.substring(Message.split(" ")[0].length() + 1);
			}

			if (!CommandEngine.callCommands(session, Label, Message.split(" "), Command))
			{
				session.getRoomUser().roomShout(this.Message, false, this.ColourId);
			}	
		}
		else
		{
			session.getRoomUser().roomShout(this.Message, false, this.ColourId);
		}
	}

}
