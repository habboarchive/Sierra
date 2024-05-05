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

public class UpdateWallItemDataComposer extends ICompose {

	private int OwnerId;
	private WallItem Item;
	
	public UpdateWallItemDataComposer(WallItem item, int ownerId) {
		super();
		this.OwnerId = ownerId;
		this.Item = item;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.UpdateWallExtraData);
		response.appendBody(this.Item);
		response.appendInt32(true);
		response.appendInt32(this.OwnerId);
		return response;
	}

}
