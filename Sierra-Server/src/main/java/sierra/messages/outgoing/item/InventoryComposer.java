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

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.inventory.Inventory;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class InventoryComposer extends ICompose {

	private ConcurrentLinkedQueue<Inventory> WallItems;
	private ConcurrentLinkedQueue<Inventory> FloorItems;
	
	public InventoryComposer(ConcurrentLinkedQueue<Inventory> wallItems, ConcurrentLinkedQueue<Inventory> floorItems) {
		super();
		this.WallItems = wallItems;
		this.FloorItems = floorItems;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.Inventory);
		response.appendInt32(1);
		response.appendInt32(1);
		response.appendInt32(this.FloorItems.size() + this.WallItems.size());

		for (Inventory item : this.FloorItems) {
			item.serializeFloorInventory(response);
		}
		
		for (Inventory item : this.WallItems) {
			item.serializeWallInventory(response);
		}
		return response;
	}

}
