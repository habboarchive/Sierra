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

import sierra.Sierra;
import sierra.events.Event;
import sierra.events.items.PickUpItemEvent;
import sierra.habbo.Session;
import sierra.habbohotel.furniture.FurnitureWalkingInteractions;
import sierra.habbohotel.pathfinder.AffectedTile;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.habbohotel.room.items.wall.WallItem;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.RemoveFloorItemComposer;
import sierra.messages.outgoing.item.RemoveWallItemComposer;
import sierra.messages.outgoing.item.UpdateInventoryComposer;
import sierra.plugin.listeners.ListenerEvent;



public class PickUpItem extends IMessage 
{
	public int Junk;
	public int Id;

	@Override
	public void handle()
	{
		for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.PICKUP_ITEM))
		{
			PickUpItemEvent Event = new PickUpItemEvent(session, Id);

			ListenEvent.Event = Event;
			ListenEvent.Listener.onPickUpItemEvent(Event);

			if (ListenEvent.Event.isCancelled())
				return;
		}

		if (!session.getRoomUser().getRoom().hasRights(false, session))
			return;

		try
		{
			FloorItem floorItem = session.getRoomUser().getRoom().getFloorItem(Id);
			WallItem wallItem = session.getRoomUser().getRoom().getWallItem(Id);

			// floor item
			if (floorItem != null && wallItem == null)
			{	
				session.sendRoom(new RemoveFloorItemComposer(Id, session.getRoomUser().getRoom().getOwnerId()));

				for (FloorItem item : session.getRoomUser().getRoom().getItemsAt(floorItem.getX(), floorItem.getY()))
				{
					Session affectedUser = session.getRoomUser().getRoom().getUserAt(item.getX(), item.getY());

					if (affectedUser != null)
					{
						if (affectedUser.getRoomUser().getStatuses().containsKey("sit"))
						{
							affectedUser.getRoomUser().getStatuses().remove("sit");
							affectedUser.getRoomUser().updateStatus();
						}

						if (affectedUser.getRoomUser().getStatuses().containsKey("lay"))
						{
							affectedUser.getRoomUser().getStatuses().remove("lay");
							affectedUser.getRoomUser().updateStatus();
						}
					}

					for (AffectedTile Tile : AffectedTile.getAffectedTilesAt(item.getItemInfo().getLength(), item.getItemInfo().getWidth(), item.getX(), item.getY(), item.getRotation()))
					{
						affectedUser = session.getRoomUser().getRoom().getUserAt(Tile.X, Tile.Y);

						if (affectedUser != null)
						{
							if (affectedUser.getRoomUser().getStatuses().containsKey("sit"))
							{
								affectedUser.getRoomUser().getStatuses().remove("sit");
								affectedUser.getRoomUser().updateStatus();
							}

							if (affectedUser.getRoomUser().getStatuses().containsKey("lay"))
							{
								affectedUser.getRoomUser().getStatuses().remove("lay");
								affectedUser.getRoomUser().updateStatus();
							}
						}
					}
				}
				

				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_inventory (`owner`, `itemid`, `extra_data`) VALUES (?, ?, ?);");
				{
					Statement.setInt(1, session.getHabbo().Id);
					Statement.setInt(2, floorItem.getItemInfo().getId());
					Statement.setString(3, floorItem.getItemInfo().getPublicName());
					Statement.executeUpdate();
				}

				Sierra.getStorage().queryParams("DELETE FROM room_items WHERE id = '" + floorItem.getId() + "'").execute();
				session.getRoomUser().getRoom().getFloorItems().remove(floorItem);
				session.getInventory().addSingle(floorItem.getId(), floorItem.getItemInfo().getId(), floorItem.ExtraData);

				/*
				 * Handle any users that were sitting on that item
				 */
			}
			else if (wallItem != null && floorItem == null)
			{
				session.sendRoom(new RemoveWallItemComposer(Id, session.getRoomUser().getRoom().getOwnerId()));

				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_inventory (`owner`, `itemid`, `extra_data`) VALUES (?, ?, ?);");
				{
					Statement.setInt(1, session.getHabbo().Id);
					Statement.setInt(2, wallItem.getItemInfo().getId());
					Statement.setString(3, wallItem.getItemInfo().getPublicName());
					Statement.execute();
				}

				Sierra.getStorage().queryParams("DELETE FROM room_items WHERE id = '" + wallItem.Id + "'").execute();
				session.getRoomUser().getRoom().getWallItems().remove(wallItem);
				session.getInventory().addSingle(wallItem.Id, wallItem.getItemInfo().getId(), wallItem.ExtraData);
			}

			session.sendResponse(new UpdateInventoryComposer());
		}
		catch (Exception e)
		{
			e.printStackTrace();

			Junk = Junk + 0;
		}
	}
}
