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

import java.util.LinkedList;

import sierra.habbohotel.pathfinder.Coord;
import sierra.habbohotel.pathfinder.Pathfinder;
import sierra.messages.IMessage;


public class Walk extends IMessage
{
	public int X;
	public int Y;

	@Override
	public void handle()
	{
		session.getRoomUser().setGoalX(X);
		session.getRoomUser().setGoalY(Y);

		try
		{     
			Pathfinder pathfinder = new Pathfinder(session);

			if (X == session.getRoomUser().getX() && Y == session.getRoomUser().getY())
				return;

			LinkedList<Coord> path = pathfinder.makePath();
			
			if(path == null)
			{
				return;
			}
			
			session.getRoomUser().setPath(path);
			session.getRoomUser().setIsWalking(true);

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}
}
