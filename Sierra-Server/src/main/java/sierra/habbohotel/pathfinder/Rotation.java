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

public class Rotation
{
	public static int Calculate(int X1, int Y1, int X2, int Y2)
    {
        int Rotation = 0;

        if (X1 > X2 && Y1 > Y2)
            Rotation = 7;
        else if (X1 < X2 && Y1 < Y2)
            Rotation = 3;
        else if (X1 > X2 && Y1 < Y2)
            Rotation = 5;
        else if (X1 < X2 && Y1 > Y2)
            Rotation = 1;
        else if (X1 > X2)
            Rotation = 6;
        else if (X1 < X2)
            Rotation = 2;
        else if (Y1 < Y2)
            Rotation = 4;
        else if (Y1 > Y2)
            Rotation = 0;
  
        return Rotation;
    }
}
