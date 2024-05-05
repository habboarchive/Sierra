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

import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.shop.items.ShopItem;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class BoughtItemComposer extends ICompose {

	private ShopItem CatalogueItem;
	private Furniture FurnitureItem;
	
	public BoughtItemComposer(ShopItem catalogueItem, Furniture furnitureItem) {
		super();
		this.CatalogueItem = catalogueItem;
		this.FurnitureItem = furnitureItem;
	}
	
	@Override
	public Response compose() {
		response.init(Outgoing.BoughtItem);
		response.appendInt32(this.CatalogueItem.Id);
		response.appendString(this.FurnitureItem.getPublicName());
		response.appendInt32(this.CatalogueItem.Credits);
		response.appendInt32(this.CatalogueItem.Pixels);
		response.appendInt32(0);
		response.appendBoolean(true);
		response.appendInt32(1);
		response.appendString(this.FurnitureItem.getType());
		response.appendInt32(this.FurnitureItem.getSpriteId());
		response.appendString("");
		response.appendInt32(1);
		response.appendInt32(0);
		response.appendString("");
		response.appendInt32(1);
		return response;
	}

}
