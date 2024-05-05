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

package sierra.messages.incoming.navigator;

import sierra.messages.IMessage;

public class LeaveRoom extends IMessage
{
	@Override
	public void handle()
	{
		/*
		 * If user is not in room we cancel the action
		 */
		if (session.getRoomUser().getInRoom())
		{
			/*
			 * User is no longer in room
			 */
			session.getRoomUser().getRoom().leaveRoom(true, true, session);
		}
	}

}
