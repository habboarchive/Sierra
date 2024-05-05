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

package sierra.habbohotel.pathfinder;

import java.awt.Point;
import java.util.LinkedList;

import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.habbohotel.room.models.RoomTileState;



public class Pathfinder
{
	private Point[] AvaliablePoints;
	private RoomTileState[][] tileState;

	public int goalX;
	public int goalY;
	private int mapX = 0;
	private int mapY = 0;

	private Session User;

	public Pathfinder(Session Session)
	{
		AvaliablePoints = new Point[]
				{ 
				new Point(0, -1),
				new Point(0, 1),
				new Point(1, 0),
				new Point(-1, 0),
				new Point(1, -1),
				new Point(-1, 1),
				new Point(1, 1),
				new Point(-1, -1)
				};

		mapX = Session.getRoomUser().getModel().getMapSizeX();
		mapY = Session.getRoomUser().getModel().getMapSizeY();
		tileState = Session.getRoomUser().getModel().getTileState();

		this.User = Session;
	}

	public LinkedList<Coord> makePath()
	{
		LinkedList<Coord> CoordSquares = new LinkedList<Coord>();

		int UserX = User.getRoomUser().getX();
		int UserY = User.getRoomUser().getY();

		goalX = User.getRoomUser().getGoalX();
		goalY = User.getRoomUser().getGoalY();

		Coord LastCoord = new Coord(-200, -200);
		int Trys = 0;

		while(true)
		{
			Trys++;
			int StepsToWalk = 10000;

			for (Point MovePoint: AvaliablePoints)
			{
				int newX = MovePoint.x + UserX;
				int newY = MovePoint.y + UserY;

				if (newX >= 0 && newY >= 0 && mapX > newX && mapY > newY && tileState[newX][newY] == RoomTileState.VALID && !User.getRoomUser().getRoom().getUserExistsAt(User.getHabbo().Id, newX, newY) && CheckFurniCoordinates(User.getRoomUser().getRoom(), newX, newY))
				{
					Coord pCoord = new Coord(newX, newY);
					pCoord.PositionDistance = DistanceBetween(newX, newY, goalX, goalY);
					pCoord.ReversedPositionDistance = DistanceBetween(goalX, goalY, newX, newY);

					if (StepsToWalk > pCoord.PositionDistance)
					{
						StepsToWalk = pCoord.PositionDistance;
						LastCoord = pCoord;
					}
				}
			}
			if (Trys >= 200)
				return null;

			else if (LastCoord.X == -200 && LastCoord.Y == -200)
				return null;

			UserX = LastCoord.X;
			UserY = LastCoord.Y;
			CoordSquares.add(LastCoord);

			if(UserX == goalX && UserY == goalY)
				break;
		}
		return CoordSquares;
	}

	public static Boolean CheckFurniCoordinates(Room room, int X, int Y)
	{
		for (FloorItem item : room.getWalkableItemsAt(X, Y).values())
		{
			return checkFurni(item);
		}
		return true;
	}

	private static boolean checkFurni(FloorItem item) 
	{
		if (item.getItemInfo().getIsType("gate") && item.ExtraData.equals("1"))
		{
			return true;
		}
		else if (item.getItemInfo().getIsType("bed"))
		{
			return true;
		}
		else if (item.getItemInfo().getCanSit())
		{
			return true;
		}
		else if (item.getItemInfo().getIsWalkable())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private int DistanceBetween(int currentX, int currentY, int goX, int goY)
	{
		return Math.abs(currentX - goX) + Math.abs(currentY - goY);
	}

}