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

package sierra.habbohotel.shop.pages;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;
import sierra.habbohotel.shop.items.ShopItem;
import sierra.habbohotel.shop.items.ShopItemEngine;





public class ShopPageEngine 
{
	private static Map<Integer, ShopPage> ShopPages = new HashMap<Integer, ShopPage>();

	public static Map<Integer, ShopPage> getShopPageMap()
	{
		return ShopPages;
	}
	
	public static ShopPage getPageAt(Integer Id)
	{
		return ShopPages.get(Id);
	}
	
	public static void load() throws Exception 
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM catalog_pages;").executeQuery();

		while (Row.next())
		{
			ShopPages.put(Row.getInt("id"), new ShopPage(Row.getInt("id"), Row.getString("caption"), Row.getString("page_layout"), Row.getString("page_headline"), Row.getString("page_teaser"), Row.getString("page_special"), Row.getString("page_text1"), Row.getString("page_text2"), Row.getString("page_text_details"), Row.getString("page_text_teaser")));
		}
		
		////Logger.writeLine("Cached [" + ShopPages.size() + "] catalogue pages");
	}

	public static ShopPage getPageByItemBase(int id) 
	{
		for (ShopPage page : ShopPages.values())
		{
			for (ShopItem item : ShopItemEngine.getItems(page.getId()))
			{
				if (item.Items.contains(id))
					return page;
			}
		}
		return null;
	} 

}
