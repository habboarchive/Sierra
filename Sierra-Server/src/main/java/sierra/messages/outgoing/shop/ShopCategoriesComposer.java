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

package sierra.messages.outgoing.shop;

import java.util.List;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.shop.cat.ShopCategory;
import sierra.habbohotel.shop.cat.ShopCategoryEngine;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class ShopCategoriesComposer extends ICompose {

	private int Rank;
	
	public ShopCategoriesComposer(int rank) {
		super();
		Rank = rank;
	}

	@Override
	public Response compose() {
		List<ShopCategory> parent = ShopCategoryEngine.getCatalogueList(this.Rank);

		response.init(Outgoing.CataIndex);
		response.appendBoolean(true);
		response.appendInt32(0);
		response.appendInt32(0);
		response.appendInt32(-1);
		response.appendString("root");
		response.appendBoolean(false);
		response.appendBoolean(false);
		response.appendInt32(parent.size());

		for (ShopCategory parentPage : parent)
		{
			List<ShopCategory> child = ShopCategoryEngine.getSubCatalogueList(parentPage.getId(), this.Rank);

			response.appendBoolean(true);
			response.appendInt32(parentPage.getColor());
			response.appendInt32(parentPage.getIcon());
			response.appendInt32(parentPage.getId());
			response.appendString(parentPage.getLabel().toLowerCase().replace(" ", "_"));
			response.appendString(parentPage.getLabel());
			response.appendInt32(child.size());

			for (ShopCategory childPage : child)
			{
				response.appendBoolean(true);
				response.appendInt32(childPage.getColor());
				response.appendInt32(childPage.getIcon());
				response.appendInt32(childPage.getId());
				response.appendString(childPage.getLabel().toLowerCase().replace(" ", "_"));
				response.appendString(childPage.getLabel());
				response.appendInt32(0);
			}
		}
		response.appendBoolean(true);
		return response;
	}

}
