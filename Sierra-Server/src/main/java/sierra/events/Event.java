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

package sierra.events;

import sierra.habbohotel.headers.Incoming;

public enum Event 
{
	CHECK_RELEASE(Incoming.CheckRelease),
	LOGIN(Incoming.LogIn),
	WALK(Incoming.Walk),
	PLACE_WALL_ITEM(Incoming.PlaceItem),
	PLACE_FLOOR_ITEM(Incoming.PlaceItem),
	PICKUP_ITEM(Incoming.PickUpItem),
	MESSENGER_SEND_MESSAGE(Incoming.PrivateChat);
	
	private int Id;

	Event(int id)
	{
		this.Id = id;
	}
	
	public int getId() {
		return Id;
	}
}