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

package sierra.habbohotel.furniture;

import sierra.habbohotel.room.RoomUser;
import sierra.habbohotel.room.items.floor.FloorItem;

public class FurnitureWalkingInteractions
{
	public static boolean handleUser(FloorItem Item, RoomUser Session)
	{
		Session.getStatuses().clear();

		if (Item.getItemInfo().getCanSit())
		{
			return walkedOnSeat(Item, Session);
		}
		else if (Item.getItemInfo().getInteractionType().equals("bed"))
		{
			return walkedOnBed(Item, Session);
		}
		else
		{
			return false;
		}
	}

	public static boolean walkedOnSeat(FloorItem Item, RoomUser user)
	{
		user.setIsSitting(true);
		user.setRotation(Item.getRotation());
		user.getStatuses().put("sit", Double.toString(Item.getHeight() + Item.getItemInfo().getStackHeight()));
		user.updateStatus();
		return true;
	}

	public static boolean walkedOnBed(FloorItem Item, RoomUser user)
	{
		user.setIsLayingDown(true);
		user.setRotation(Item.getRotation());
		user.getStatuses().put("lay", Double.toString(Item.getHeight() + Item.getItemInfo().getStackHeight()));
		user.updateStatus();
		return true;
	}

}
