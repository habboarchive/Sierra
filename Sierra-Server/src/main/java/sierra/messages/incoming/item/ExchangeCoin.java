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
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.habbohotel.shop.items.ShopItemEngine;
import sierra.messages.IMessage;
import sierra.messages.incoming.user.UserCredits;

public class ExchangeCoin extends IMessage 
{
	public int Id;
	
	@Override
	public void handle() throws Exception
	{
		if (!session.getRoomUser().isOwner())
			return;
		
		FloorItem Item = session.getRoomUser().getRoom().getFloorItem(this.Id);
		
		if (!Item.getItemInfo().getItemName().startsWith("CF_"))
			return;
		
		session.getHabbo().Credits += ShopItemEngine.getItemByBaseId(Item.getItemInfo().getId()).Credits;
		session.getHabbo().save();
		
		Sierra.getSocketFactory().getMessageHandler().invokePacket(session, null, UserCredits.class);
		
		session.getResponse().init(Outgoing.RemoveFloorItem);
		session.getResponse().appendString(Id);
		session.getResponse().appendInt32(0);
		session.getResponse().appendInt32(session.getHabbo().Id);
		//session.sendRoom();
		
		Sierra.getStorage().queryParams("DELETE FROM room_flooritems WHERE id = '" + Id + "'").execute();

		session.getRoomUser().getRoom().getFloorItems().remove(Item);
		
		session.getResponse().init(Outgoing.RemoveFloorItem);
		session.getResponse().appendString(Id);
		//session.sendRoom();
	}

}
