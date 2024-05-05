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

import sierra.habbohotel.room.items.wall.WallItem;
import sierra.messages.IMessage;
import sierra.messages.outgoing.item.UpdateWallItemDataComposer;

public class ChangeWallItemState extends IMessage 
{
	public int Id;
	
	@Override
	public void handle()
	{
		if (!session.getRoomUser().getRoom().hasRights(false, session))
			return;

		WallItem item = session.getRoomUser().getRoom().getWallItem(Id);

		if (item == null)
			return;

		item.changeExtraData();
		item.saveExtraData();
		
		session.sendRoom(new UpdateWallItemDataComposer(item, session.getHabbo().Id));
	}

}
