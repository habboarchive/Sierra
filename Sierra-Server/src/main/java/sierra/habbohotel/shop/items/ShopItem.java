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

import java.util.ArrayList;
import java.util.List;

import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.netty.readers.Response;



public class ShopItem 
{
	public int Id;
	public int PageId;
	public int Credits;
	public int Pixels;
	public int Snow;
	public int Amount;
	public String ItemIdString;
	public String Name;
	public Boolean VIP;
	public List<Integer> Items;
	
	public ShopItem(int Id, int PageId, String Name, String ItemIds, int Credits, int Pixels, int Snow, int Amount, int VIP)
	{
		this.Id = Id;
		this.PageId = PageId;
		this.Name = Name;
		this.Credits = Credits;
		this.Pixels = Pixels;
		this.Snow = Snow;
		this.Amount = Amount;
		this.ItemIdString = ItemIds;
		this.VIP = VIP == 1;
		this.Items = new ArrayList<Integer>();
		
        if (this.ItemIdString.contains(";"))
        {
            String[] strArray = this.ItemIdString.split(";");
            for (String str : strArray)
            {
                if (str != "")
                {
                    this.Items.add(Integer.valueOf(str));
                }
            }
        }
        else
        {
            this.Items.add(Integer.valueOf(this.ItemIdString));
        }
		
	}
	
	public void Serialize(Response Message, String Layout, int baseId)
	{
        Message.appendString(getItemInfo(baseId).getType());
		Message.appendInt32(getItemInfo(baseId).getSpriteId());

		if (Layout.equals("spaces"))
		{
			if (Name.contains("wallpaper") || Name.contains("floor") || Name.contains("landscape"))
			{
				Message.appendString(Name.split("_")[2]);
			}
		}
		else
		{
			Message.appendString("");
		}

		Message.appendInt32(Amount);
		Message.appendInt32(0);
	}
	
	public Furniture getItemInfo() {
		return FurnitureEngine.get(Items.get(0));
	}
	
	public Furniture getItemInfo(int itemId) {
		return FurnitureEngine.get(itemId);
	}
}
