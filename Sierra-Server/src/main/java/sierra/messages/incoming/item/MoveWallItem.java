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

import sierra.Sierra;
import sierra.habbohotel.room.items.wall.WallItem;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.UpdateWallItemComposer;

public class MoveWallItem extends IMessage 
{
	public int Id;
	public String Position;
	
	@Override
	public void handle() 
	{		
		if (!session.getRoomUser().getRoom().hasRights(false, session))
			return;

		WallItem Item = session.getRoomUser().getRoom().getWallItem(this.Id);
		
		if (Item == null)
			return;

		Item.Position = Position;

		Sierra.getStorage().executeQuery("UPDATE room_items SET position = '" + Position + "' WHERE id = '" + Id + "'");
		
		session.sendRoom(new UpdateWallItemComposer(Item, session.getRoomUser().getRoom().getOwnerId(), session.getRoomUser().getRoom().getOwnerName()));
		
		session.getRoomUser().getRoom().getWallItems().remove(Item);
		session.getRoomUser().getRoom().getWallItems().add(new WallItem(Item.Id, Item.getItemInfo().getId(), Item.Position, Item.ExtraData));
	}

}
