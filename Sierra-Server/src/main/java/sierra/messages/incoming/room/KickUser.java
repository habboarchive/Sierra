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

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.IMessage;

public class KickUser extends IMessage {

	public int UserId;
	
	@Override
	public void handle() throws Exception {
		
		if (!session.getRoomUser().getRoom().hasRights(false, session)) 
			return;
		
		Session user = Sierra.getSocketFactory().getSessionManager().getUserById(UserId);
		
		if (user == null)
		{
			return;
		}
		
		if (!user.getRoomUser().getInRoom())
		{
			return;
		}
		
		if (user.getRoomUser().getRoom() != session.getRoomUser().getRoom())
		{
			return;
		}
		
		if (user.getHabbo().hasFuse("fuse_kick"))
		{
			return;
		}
		
		if (user.getRoomUser().getRoom().hasRights(false, user))
		{
			return;
		}
		
		if (user.getRoomUser().isOwner())
		{
			return;
		}
		
		user.getRoomUser().getRoom().leaveRoom(true, false, user);
		user.sendNotify("You have been kicked from the room!");
	}

}
