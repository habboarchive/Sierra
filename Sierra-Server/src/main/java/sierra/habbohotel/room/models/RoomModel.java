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

package sierra.habbohotel.room.models;

public class RoomModel 
{
	private String Name;
	private String Heightmap;
	private String Map = "";
	private String RelativeMap = "";
	private int DoorX;
	private int DoorY;
	private int DoorZ;
	private int DoorRot;
	private int MapSizeX;
	private int MapSizeY;
	private int MapSize;
	private int OPEN = 0;
	private int CLOSED = 1;
	private int[][] Squares;
	private double[][] SquareHeight;
	private RoomTileState[][] SquareState;

	public RoomModel(String Name, String Heightmap, int DoorX, int DoorY, int DoorZ, int DoorRot)
	{
		this.Name = Name;
		this.Heightmap = Heightmap;
		this.DoorX = DoorX;
		this.DoorY = DoorY;
		this.DoorZ = DoorZ;
		this.DoorRot = DoorRot;

		String[] Temporary = Heightmap.split(Character.toString((char)13));

		this.MapSizeX = Temporary[0].length();
		this.MapSizeY = Temporary.length;
		this.Squares = new int[MapSizeX][MapSizeY];
		this.SquareHeight = new double[MapSizeX][MapSizeY];
		this.SquareState = new RoomTileState[MapSizeX][MapSizeY];


		for (int y = 0; y < MapSizeY; y++)
		{
			if (y > 0)
			{
				Temporary[y] = Temporary[y].substring(1);
			}

			for (int x = 0; x < MapSizeX; x++)
			{
				String Square = Temporary[y].substring(x,x + 1).trim().toLowerCase();

				if (Square.equals("x"))
				{
					SquareState[x][y] = RoomTileState.INVALID;
					Squares[x][y] = CLOSED;
				}
				else if(isNumeric(Square))
				{
					SquareState[x][y] = RoomTileState.VALID;
					Squares[x][y] = OPEN;
					SquareHeight[x][y] = Double.parseDouble(Square);
					MapSize++;
				}

				if (this.DoorX == x && this.DoorY == y)
				{
					SquareState[x][y] = RoomTileState.VALID;
					RelativeMap += (int)DoorZ + "";
				}
				else
				{
					if(Square.isEmpty() || Square == null)
					{
						continue;
					}
					RelativeMap += Square;
				}
			}
			RelativeMap += (char)13;
		}

		for(String MapLine: Heightmap.split("\r\n"))
		{
			if(MapLine.isEmpty() || MapLine == null)
			{
				continue;
			}
			Map += MapLine + (char)13;
		}
	}
	
	/*public int[] getRandomCoords(int Id, Room room)
	{
		Random rand = new Random();
		
		int randomX = rand.nextInt(this.MapSizeX);
		int randomY = rand.nextInt(this.MapSizeY);
		
		if (this.SquareState[randomX][randomY] == RoomTileState.INVALID && !room.getEntityExistsAt(Id, randomX, randomY) && !room.getUserExistsAt(Id, randomX, randomY) && !Pathfinder.CheckFurniCoordinates(room, randomX, randomY))
			return this.getRandomCoords(Id, room);
		
		return new int[] { randomX, randomY };
	}*/
	
	public String getHeightmap() {
		return Heightmap;
	}
	
	public String getRelativeHeightmap() {
		return RelativeMap;
	}
	
	private boolean isNumeric(String Input)
	{
		try
		{
			Integer.parseInt(Input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public String getName() {
		return Name;
	}
	
	public String getMap() {
		return Map;
	}
	
	public String getRelativeMap() {
		return RelativeMap;
	}
	
	public int getDoorX() {
		return DoorX;
	}
	
	public int getDoorY() {
		return DoorY;
	}
	
	public int getDoorZ() {
		return DoorZ;
	}
	
	public int getDoorRot() {
		return DoorRot;
	}
	
	public int getMapSizeX() {
		return MapSizeX;
	}
	
	public int getMapSizeY() {
		return MapSizeY;
	}
	
	public int getMapSize() {
		return MapSize;
	}

	public RoomTileState[][] getTileState() {
		return SquareState;
	}
	
	public double[][] getSquareHeight() {
		return SquareHeight;
	}
	
	public int[][] getSqState() {
		return Squares;
	}
}