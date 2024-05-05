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

package sierra.habbohotel.furniture.interactions;

import sierra.habbo.Session;
import sierra.habbohotel.room.items.floor.FloorItem;

public class TeleportAction implements Interaction {

	@Override
	public void Interact(Boolean State, Session Session, FloorItem Item)
	{
		if (Item.ExtraData.equals("2"))
			Item.ExtraData = "1";

		if (Item.ExtraData.equals("1"))
			Item.ExtraData = "0";
		
		if (Item.ExtraData.equals("0"))
			Item.ExtraData = "1";
		
		Item.sendUpdate(Session);
		Item.saveExtraData();
	}
}
