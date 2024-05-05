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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.events.Event;
import sierra.events.items.PlaceFloorItemEvent;
import sierra.events.items.PlaceWallItemEvent;
import sierra.habbohotel.inventory.Inventory;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.SendFloorItemComposer;
import sierra.messages.outgoing.item.SendWallItemComposer;
import sierra.plugin.listeners.ListenerEvent;



public class PlaceItem extends IMessage 
{
	public String PlaceData;

	@Override
	public void handle()
	{
		String[] Bits = PlaceData.split(" ");
		
		int Id = Integer.parseInt(Bits[0].replace("-", ""));
		
		try
		{
			if (!session.getRoomUser().getRoom().hasRights(false, session))
				return;

			if (Bits[1].startsWith(":"))
			{		
				Inventory Item = session.getInventory().getWallItem(Id);

				String Position = Bits[1] + " " + Bits[2] + " " + Bits[3];
				
				PlaceWallItemEvent event = null;
				for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.PLACE_WALL_ITEM))
				{
					event = new PlaceWallItemEvent(session, Item, Item.getItemInfo(), Id, Bits[1] + " " + Bits[2] + " " + Bits[3]);

					ListenEvent.Event = event;
					ListenEvent.Listener.onPlaceWallItemEvent(event);
					
					if (ListenEvent.Event.isCancelled())
						return;
					else
					{
						Position = event.getPlacement();
						Item = event.getInventory();
					}
				}
				
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO room_items (`roomid`, `baseid`, `x`, `y`, `rotation`, `position`, `is_wall`, `height`, `extra`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				{
					Statement.setInt(1, session.getRoomUser().getRoom().getId());
					Statement.setInt(2, Item.getItemInfo().getId());
					Statement.setInt(3, 0);
					Statement.setInt(4, 0);
					Statement.setInt(5, 0);
					Statement.setString(6, Position);
					Statement.setInt(7, 1);
					Statement.setFloat(8, 0);
					Statement.setString(9, "0");
					Statement.executeUpdate();

					ResultSet Keys = Statement.getGeneratedKeys();

					while (Keys.next())
					{
						Id = Keys.getInt(1);
					}
				}

				Sierra.getStorage().executeQuery("DELETE FROM members_inventory WHERE id = '" + Item.getId() + "'");

				session.sendRoom(new SendWallItemComposer(session.getRoomUser().getRoom().addWallItem(Id, Item.getItemInfo().getId(), Position, "0"), session.getRoomUser().getRoom().getOwnerId()));
				session.getInventory().removeItem(session, Item, false);

			}
			else
			{
				Integer X = Integer.parseInt(Bits[1]);
				Integer Y = Integer.parseInt(Bits[2]);
				Integer Rot = Integer.parseInt(Bits[3]);
				
				Inventory Item = session.getInventory().getFloorItem(Id);

				float Height = (float)session.getRoomUser().getModel().getSquareHeight()[X][Y];

				for (FloorItem stackItem : session.getRoomUser().getRoom().getItemsAt(X, Y))
				{
					if (Item.getId() != stackItem.getId())
						if (stackItem.getItemInfo().getCanStack())
							Height += stackItem.getItemInfo().getStackHeight();
				}
				
				PlaceFloorItemEvent event = null;
				
				for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.PLACE_FLOOR_ITEM))
				{
					event = new PlaceFloorItemEvent(session, Item, Item.getItemInfo(), Id, X, Y, Rot, Height);

					ListenEvent.Event = event;
					ListenEvent.Listener.onPlaceFloorItemEvent(event);
					
					if (ListenEvent.Event.isCancelled())
						return;
					else
					{
						X = event.getX();
						Y = event.getY();
						Rot = event.getRotation();
						Item = event.getInventory();
					}
				}

				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO room_items (`roomid`, `baseid`, `x`, `y`, `rotation`, `position`, `is_wall`, `height`, `extra`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				{
					Statement.setInt(1, session.getRoomUser().getRoom().getId());
					Statement.setInt(2, Item.getItemInfo().getId());
					Statement.setInt(3, X);
					Statement.setInt(4, Y);
					Statement.setInt(5, Rot);
					Statement.setString(6, "");
					Statement.setInt(7, 0);
					Statement.setFloat(8, Height);
					Statement.setString(9, "0");
					Statement.executeUpdate();

					ResultSet Keys = Statement.getGeneratedKeys();

					while (Keys.next())
					{
						Id = Keys.getInt(1);
					}
				}

				session.sendRoom(new SendFloorItemComposer(session.getRoomUser().getRoom().addFloorItem(Id, Item.getItemInfo().getId(), X, Y, Rot, Height, "0"), session.getRoomUser().getRoom().getOwnerId(), session.getRoomUser().getRoom().getOwnerName()));
				session.getInventory().removeItem(session, Item, true);
			}
		}
		catch (Exception e)
		{
			
		}
	}
}