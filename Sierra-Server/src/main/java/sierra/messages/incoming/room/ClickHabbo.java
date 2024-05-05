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

import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.pathfinder.Rotation;
import sierra.messages.IMessage;
import sierra.messages.outgoing.user.UserBadgesComposer;



public class ClickHabbo extends IMessage 
{
	public int Id;

	@Override
	public void handle()
	{
		if (!session.getRoomUser().getInRoom())
			return;

		Session habbo = Sierra.getSocketFactory().getSessionManager().getUserById(this.Id);

		if (!habbo.getRoomUser().getInRoom())
			return;

		if (habbo.getHabbo().Id != session.getHabbo().Id && !session.getRoomUser().getIsSitting()) 
		{
			session.getRoomUser().setRotation(Rotation.Calculate(session.getRoomUser().getX(), session.getRoomUser().getY(), habbo.getRoomUser().getX(), habbo.getRoomUser().getY()));
			session.getRoomUser().updateStatus();
		}
		
		Map<Integer, String> Badges = new HashMap<Integer, String>();
		
		if (habbo.getHabbo().Username.toLowerCase().equals("alex") || habbo.getHabbo().Username.toLowerCase().equals("generik"))
		{
			Badges.put(0, "ADM");
			Badges.put(0, "BE2");
		}
		else
		{
			Badges.put(0, "BE2");
		}
		
		session.sendResponse(new UserBadgesComposer(habbo.getHabbo().Id, Badges));
		
		Badges.clear();
	}
}
