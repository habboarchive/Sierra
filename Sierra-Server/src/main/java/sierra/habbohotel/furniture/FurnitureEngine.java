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

package sierra.habbohotel.furniture;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;



public class FurnitureEngine
{
	private static Map<Integer, Furniture> Furnitures;

	static {
		Furnitures = new HashMap<Integer, Furniture>();
	}
	
	public static Map<Integer, Furniture> furnitureMap() {
		return Furnitures;
	}
	
	public static Furniture get(Integer ItemId) {
		return Furnitures.get(ItemId);
	}
	
	public static void load() throws Exception
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM furniture;").executeQuery();

		while (Row.next())
		{
			Furnitures.put(Row.getInt("id"), new Furniture(Row.getInt("id"), Row.getString("public_name"), Row.getString("item_name"), Row.getString("type"),	Row.getInt("width"), Row.getInt("length"), Row.getFloat("stack_height"), Row.getInt("can_stack"),	Row.getInt("can_sit"), Row.getInt("is_walkable"), Row.getInt("sprite_id"), Row.getInt("allow_recycle"),	Row.getInt("allow_trade"), Row.getInt("allow_marketplace_sell"), Row.getInt("allow_gift"), Row.getInt("allow_inventory_stack"), Row.getString("interaction_type"),	Row.getInt("interaction_modes_count"), Row.getString("vending_ids"), Row.getInt("is_arrow")));
		}
	}
}
