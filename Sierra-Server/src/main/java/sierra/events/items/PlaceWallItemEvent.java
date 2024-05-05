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

public class PlaceWallItemEvent extends IEvent
{
	private int Id;
	private String Placement;

	private Inventory inventory;
	private Furniture Furniture;

	public PlaceWallItemEvent(Session session, Inventory inventory, Furniture furniture, int Id, String placement) 
	{	
		super(session);
		
		this.Id = Id;
		this.inventory = inventory;
		this.Placement = placement;
	}
	
	public int getId() {
		return Id;
	}
	
	public Furniture getFurniture() {
		return Furniture;
	}
	
	public String getPlacement() {
		return Placement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setPlacement(String placement) {
		Placement = placement;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
