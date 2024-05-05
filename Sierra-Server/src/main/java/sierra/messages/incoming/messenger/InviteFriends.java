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

package sierra.messages.incoming.messenger;

import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.messages.IMessage;
import sierra.messages.outgoing.messenger.InviteComposer;
import sierra.utils.UserInputFilter;




public class InviteFriends extends IMessage 
{
	private List<Integer> Users;
	
	public int Amount;
	private String Message;

	@Override
	public void handle()
	{
		this.Users = new ArrayList<Integer>();
		
		for (int i = 0; i < Amount; i++)
		{
			this.Users.add(this.request.readInt());
		}
		
		this.Message = UserInputFilter.filterString(this.request.readString(), true);
		
		
		for (int i : Users)
		{
			if (!session.getMessenger().isBuddy(i))
				continue;
			
			Sierra.getSocketFactory().getSessionManager().getUserById(i).sendResponse(new InviteComposer(session.getHabbo().Id, Message));
		}
		
		Users.clear();
	}
}
