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

import java.util.ArrayList;
import java.util.List;

public class AffectedTile
{
	public int X;
	public int Y;
	public int I;
	
	public AffectedTile()
	{
		X = 0;
		Y = 0;
		I = 0;
	}
	
	public AffectedTile(int x, int y, int i)
	{
		X = x;
		Y = y;
		I = i;
	}
	
	public static List<AffectedTile> getAffectedTilesAt(int Length, int Width, int PosX, int PosY, int Rotation)
	{
		List<AffectedTile> PointList = new ArrayList<AffectedTile>();

		if (Length > 1)
		{
			if (Rotation == 0 || Rotation == 4)
			{
				for (int i = 1; i < Length; i++)
				{
					PointList.add(new AffectedTile(PosX, PosY + i, i));

					for (int j = 1; j < Width; j++)
					{
						PointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
					}
				}
			}
			else if (Rotation == 2 || Rotation == 6)
			{
				for (int i = 1; i < Length; i++)
				{
					PointList.add(new AffectedTile(PosX + i, PosY, i));

					for (int j = 1; j < Width; j++)
					{
						PointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
					}
				}
			}
		}

		if (Width > 1)
		{
			if (Rotation == 0 || Rotation == 4)
			{
				for (int i = 1; i < Width; i++)
				{
					PointList.add(new AffectedTile(PosX + i, PosY, i));

					for (int j = 1; j < Length; j++)
					{
						PointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
					}
				}
			}
			else if (Rotation == 2 || Rotation == 6)
			{
				for (int i = 1; i < Width; i++)
				{
					PointList.add(new AffectedTile(PosX, PosY + i, i));

					for (int j = 1; j < Length; j++)
					{
						PointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
					}
				}
			}
		}
		return PointList;
	}
}