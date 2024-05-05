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

package sierra.messages.incoming.item;

import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbohotel.inventory.Inventory;
import sierra.habbohotel.room.items.wall.DecorationType;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.PaperComposer;



public class AddPapers extends IMessage
{
	public int Id;
	
	@Override
	public void handle()
	{
		try
		{
			if (!session.getRoomUser().getRoom().hasRights(false, session))
				return;

			Inventory Item = session.getInventory().getItem(this.Id);

			DecorationType type = null;
			
			if (Item.getItemName().contains("wall"))
				type = DecorationType.WALL;
			if (Item.getItemName().contains("landscape"))
				type = DecorationType.LANDSCAPE;
			if (Item.getItemName().contains("floor"))
				type = DecorationType.FLOOR;

			String ExtraData = Item.getExtraData();

			if (type == DecorationType.WALL)
			{
				session.getRoomUser().getRoom().setWall(ExtraData);
				session.sendRoom(new PaperComposer("wallpaper", session.getRoomUser().getRoom().getWall()));

				Sierra.getStorage().queryParams("UPDATE rooms SET wallpaper = '" + ExtraData + "' WHERE id = '" + session.getRoomUser().getRoom().getId() + "'").execute();
			}
			if (type == DecorationType.LANDSCAPE)
			{
				session.getRoomUser().getRoom().setLandscape(ExtraData);
				session.sendRoom(new PaperComposer("landscape", session.getRoomUser().getRoom().getLandscape()));

				Sierra.getStorage().queryParams("UPDATE rooms SET outside = '" + ExtraData + "' WHERE id = '" + session.getRoomUser().getRoom().getId() + "'").execute();
			}
			if (type == DecorationType.FLOOR)
			{
				session.getRoomUser().getRoom().setFloor(ExtraData);
				session.sendRoom(new PaperComposer("floor", session.getRoomUser().getRoom().getFloor()));

				Sierra.getStorage().queryParams("UPDATE rooms SET floor = '" + ExtraData + "' WHERE id = '" + session.getRoomUser().getRoom().getId() + "'").execute();
			}
			
			Sierra.getStorage().queryParams("DELETE FROM members_inventory WHERE owner = '" + session.getHabbo().Id + "'").execute();

			session.getInventory().removeItem(session, Item, false);
		}
		catch (SQLException e)
		{
			
		}
	}
}
