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

package sierra.messages.outgoing.item;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.items.wall.WallItem;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class UpdateWallItemComposer extends ICompose {

	private WallItem Item;
	private Integer OwnerId;
	private String OwnerName;
	
	public UpdateWallItemComposer(WallItem item, Integer ownerId, String ownerName) {
		super();
		this.Item = item;
		this.OwnerId = ownerId;
		this.OwnerName = ownerName;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.UpdateWallItem);
		response.appendString(Item.Id);
		response.appendInt32(Item.getItemInfo().getSpriteId());
		response.appendString(Item.Position);
		response.appendString(Item.ExtraData);
		response.appendInt32(Item.getItemInfo().getInteractionType().equals("default") ? 1 : 0);
		response.appendInt32(this.OwnerId);
		response.appendString(this.OwnerName);
		return response;
	}

}
