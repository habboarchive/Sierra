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

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.IMessage;
import sierra.messages.outgoing.messenger.FollowBuddyComposer;
import sierra.messages.outgoing.room.RoomDataComposer;


public class FollowFriend extends IMessage 
{
	public int Id;

	@Override
	public void handle()
	{
		Session follow = Sierra.getSocketFactory().getSessionManager().getUserById(this.Id);
		
		if (follow == null)
		{
			return;
		}
		
		if (follow.getRoomUser().getInRoom() == false)
		{
			return;
		}
		
		session.sendResponse(new FollowBuddyComposer(follow.getRoomUser().getRoom().getId()));
        session.sendResponse(new RoomDataComposer(follow.getRoomUser().getRoom()));
        
        session.getMessenger().setIsFollowing(true);
	}
}
