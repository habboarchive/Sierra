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

import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.furniture.FurnitureWalkingInteractions;
import sierra.habbohotel.pathfinder.AffectedTile;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.UpdateFloorItemComposer;



public class MoveFloorItem extends IMessage
{
	public int Id;
	public int X;
	public int Y;
	public int R;

	@Override
	public void handle()
	{
		try
		{
			if (!session.getRoomUser().getRoom().hasRights(false, session))
				return;

			FloorItem Item = session.getRoomUser().getRoom().getFloorItem(this.Id);

			if (Item == null)
				return;

			Item.setX(this.X);
			Item.setY(this.Y);
			Item.setRotation(this.R);

			float Height = (float)session.getRoomUser().getModel().getSquareHeight()[this.X][this.Y];

			for (FloorItem stackItem : session.getRoomUser().getRoom().getItemsAt(this.X, this.Y))
			{
				if (Item.getId() != stackItem.getId())
				if (stackItem.getItemInfo().getCanStack())
						Height += stackItem.getItemInfo().getStackHeight();
			}

			Item.setHeight(Height);

			Sierra.getStorage().executeQuery("UPDATE room_items SET x = '" + X + "', y ='" + Y  + "', rotation = '" + R + "', height = '" + Height + "' WHERE id = '" + Id + "'");

			session.sendRoom(new UpdateFloorItemComposer(Item, session.getRoomUser().getRoom().getOwnerId()));

			session.getRoomUser().getRoom().getFloorItems().remove(Item);
			session.getRoomUser().getRoom().getFloorItems().add(new FloorItem(Item.getId(), Item.getBaseId(), X, Y, R, Height, Item.ExtraData));

			for (FloorItem item : session.getRoomUser().getRoom().getItemsAt(X, Y))
			{
				Session affectedUser = session.getRoomUser().getRoom().getUserAt(Item.getX(), Item.getY());

				if (affectedUser != null)
					FurnitureWalkingInteractions.handleUser(item, affectedUser.getRoomUser());

				for (AffectedTile Tile : AffectedTile.getAffectedTilesAt(Item.getItemInfo().getLength(), Item.getItemInfo().getWidth(), Item.getX(), Item.getY(), Item.getRotation()))
				{
					affectedUser = session.getRoomUser().getRoom().getUserAt(Tile.X, Tile.Y);

					if (affectedUser != null)
						FurnitureWalkingInteractions.handleUser(item, affectedUser.getRoomUser());
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}