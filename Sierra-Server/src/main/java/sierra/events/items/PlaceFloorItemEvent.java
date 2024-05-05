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

package sierra.events.items;

import sierra.events.IEvent;
import sierra.habbo.Session;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.inventory.Inventory;

public class PlaceFloorItemEvent extends IEvent
{
	private int Id;
	private int X;
	private int Y;
	private int Rotation;
	private float Height;
	
	private Inventory inventory;
	private Furniture furniture;

	public PlaceFloorItemEvent(Session session, Inventory inventory, Furniture furniture, int Id, int x, int y, int rotation, float height) 
	{	
		super(session);
		
		this.inventory = inventory;
		this.furniture = furniture;
		this.Id = Id;
		this.X = x;
		this.Y = y;
		this.Rotation = rotation;
		this.Height = height;
	}

	public int getId() {
		return Id;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getRotation() {
		return Rotation;
	}

	public float getHeight() {
		return Height;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Furniture getFurniture() {
		return furniture;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setX(int x) {
		X = x;
	}

	public void setY(int y) {
		Y = y;
	}

	public void setRotation(int rotation) {
		Rotation = rotation;
	}

	public void setHeight(float height) {
		Height = height;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
