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
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.habbohotel.room.items.wall.WallItem;

public class PickUpItemEvent extends IEvent
{
	private int Id;
	private boolean IsWallItem;
	private boolean IsFloorItem;
	
	private FloorItem FloorItem;
	private WallItem WallItem;
	
	public PickUpItemEvent(Session session, int Id)
	{
		super(session);
		
		this.Id = Id;	
		this.FloorItem = session.getRoomUser().getRoom().getFloorItem(Id);
		this.WallItem = session.getRoomUser().getRoom().getWallItem(Id);
		
		this.IsWallItem = FloorItem == null;
		this.IsFloorItem = WallItem == null;
	}

	public int getId() {
		return Id;
	}

	public Boolean isWallItem() {
		return IsWallItem;
	}

	public Boolean isFloorItem() {
		return IsFloorItem;
	}

	public FloorItem getFloorItem() {
		return FloorItem;
	}

	public WallItem getWallItem() {
		return WallItem;
	}

}
