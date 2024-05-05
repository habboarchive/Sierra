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

package sierra.habbohotel.shop.items;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sierra.Sierra;



public class ShopItemEngine
{
	private static Map<Integer, ShopItem> CatalogueItems = new HashMap<Integer, ShopItem>();

	public static Map<Integer, ShopItem> shopItems()
	{
		return CatalogueItems;
	}
	
	public static ShopItem getItemByBaseId(int Id)
	{
		for (ShopItem Item : CatalogueItems.values())
		{
			if (Item.Items.contains(Id))
			{
				return Item;
			}
		}
		return null;
	}
	
	public static List<ShopItem> getItems(Integer PageId)
	{
		List<ShopItem> PageItems = new ArrayList<ShopItem>();
		
		for (ShopItem Item : CatalogueItems.values())
		{
			if (Item.PageId == PageId)
			{
				PageItems.add(Item);
			}
		}
		return PageItems;
	}
	public static void load() throws Exception 
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM catalog_items;").executeQuery();

		while (Row.next())
		{
			CatalogueItems.put(Row.getInt("id"), new ShopItem(Row.getInt("id"), Row.getInt("page_id"), Row.getString("catalog_name"), Row.getString("item_ids"), Row.getInt("cost_credits"), Row.getInt("cost_pixels"), Row.getInt("cost_snow"), Row.getInt("amount"), Row.getInt("vip")));
		}
		
		//Logger.writeLine("Cached [" + CatalogueItems.size() + "] catalogue items");
	}
}
 