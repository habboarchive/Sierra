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
import sierra.habbohotel.shop.items.ShopItem;
import sierra.habbohotel.shop.items.ShopItemEngine;
import sierra.habbohotel.shop.pages.ShopPage;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class ShopPageComposer extends ICompose {

	private ShopPage Page;

	public ShopPageComposer(ShopPage page) {
		super();
		this.Page = page;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.CataPage);
		response.appendInt32(Page.getId());

		if (Page.getLayout().equals("frontpage"))
		{
			response.appendString("frontpage3");
			response.appendInt32(3);
			response.appendString(Page.getHeadline());
			response.appendString(Page.getTeaser());
			response.appendString("");
			response.appendInt32(11);
			response.appendString(Page.getSpecial());
			response.appendString(Page.getText1());
			response.appendString("");
			response.appendString(Page.getText2());
			response.appendString(Page.getDetails());
			response.appendString(Page.getTeaser2());
			response.appendString("Rares");
			response.appendString("#FEFEFE");
			response.appendString("#FEFEFE");
			response.appendString("Click here for more info..");
			response.appendString("magic.credits");
		}
		else if (Page.getLayout().equals("spaces"))
		{
			response.appendString("spaces_new");
			response.appendInt32(1);
			response.appendString(Page.getHeadline());
			response.appendInt32(1);
			response.appendString(Page.getText1());
		}
		else if (Page.getLayout().equals("trophies"))
		{
			response.appendString("trophies");
			response.appendInt32(1);
			response.appendString(Page.getHeadline());
			response.appendInt32(2);
			response.appendString(Page.getText1());
			response.appendString(Page.getDetails());
		}
		else if (Page.getLayout().equals("pets"))
		{
			response.appendString("pets");
			response.appendInt32(2);
			response.appendString(Page.getHeadline());
			response.appendString(Page.getTeaser());
			response.appendInt32(4);
			response.appendString(Page.getText1());
			response.appendString("Give a name:");
			response.appendString("Pick a color:");
			response.appendString("Pick a race:");
		}
		else if (Page.getLayout().equals("guild_frontpage"))
		{
			response.appendString(Page.getLayout());
			response.appendInt32(2);
			response.appendString("catalog_groups_en");
			response.appendString("");
			response.appendInt32(3);
			response.appendString(Page.getTeaser());
			response.appendString(Page.getSpecial());
			response.appendString(Page.getText1());
		}
		else if (Page.getLayout().equals("club_buy"))
		{
			response.appendString("vip_buy");
			response.appendInt32(2);
			response.appendString("ctlg_buy_vip_header");
			response.appendString("ctlg_gift_vip_teaser");
			response.appendInt32(0);
		}
		else
		{
			response.appendString(Page.getLayout());
			response.appendInt32(3);
			response.appendString(Page.getHeadline());
			response.appendString(Page.getTeaser());
			response.appendString(Page.getSpecial());
			response.appendInt32(3);
			response.appendString(Page.getText1());
			response.appendString(Page.getDetails());
			response.appendString(Page.getTeaser2());
		}

		if (!Page.getLayout().equals("frontpage") && !Page.getLayout().equals("club_buy"))
		{
			List<ShopItem> PageItems = ShopItemEngine.getItems(Page.getId());

			response.appendInt32(PageItems.size());

			for (ShopItem Item : PageItems)
			{
				response.appendInt32(Item.Id);
				response.appendString(Item.Name);
				response.appendInt32(Item.Credits);
				response.appendInt32(Item.Pixels);
				response.appendInt32(0);
				response.appendBoolean(true);
				
				response.appendInt32(Item.Items.size());

				for (int i : Item.Items)
				{
					Item.Serialize(response, Page.getLayout(), i);
				}
				
				response.appendBoolean(false);
				response.appendBoolean(false);
				
			}
		}
		else
			response.appendInt32(0);

		response.appendInt32(-1);
		response.appendBoolean(false);
		return response;
	}
	
}
