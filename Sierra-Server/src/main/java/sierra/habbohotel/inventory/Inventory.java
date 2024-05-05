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

package sierra.habbohotel.inventory;

import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.netty.readers.Response;

public class Inventory
{
	private int Id;
	private int BaseId;
	private String ExtraData;
	private String Label;
	
	public Inventory(int Id, int BaseId, String Label)
	{
		this.Id = Id;
		this.BaseId = BaseId;
		this.Label = Label;
		
		if (Label.contains("_single_") || Label.contains("landscape"))
		{
			this.ExtraData = Label.split("_")[2];
		}
		else
		{
			this.ExtraData = "";
		}
	}
	
	public int getId() 
	{
		return Id;
	}
	
	public String getExtraData()
	{
		return ExtraData;
	}
	
	public String getItemName()
	{
		return Label;
	}
	
	public Furniture getItemInfo()
	{
		return FurnitureEngine.get(this.BaseId);
	}
	
	public void serializeItemPermissions(Response msg)
	{

	}
	
	public void serializeFloorInventory(Response msg)
	{
		msg.appendInt32(this.Id);
		msg.appendString(this.getItemInfo().getType().toUpperCase());
		msg.appendInt32(this.Id);
		msg.appendInt32(this.getItemInfo().getSpriteId());
		msg.appendInt32(1);
		msg.appendString("");
		msg.appendInt32(0);
		
		msg.appendBoolean(this.getItemInfo().getAllowRecycle());
		msg.appendBoolean(this.getItemInfo().getAllowTrade());
		msg.appendBoolean(this.getItemInfo().getAllowInventoryStack());
		msg.appendBoolean(this.getItemInfo().getAllowMarketplaceSell());
		msg.appendInt32(-1);
		msg.appendBoolean(true);
        msg.appendInt32(-1);
		
		msg.appendString("");
		msg.appendInt32(0);
	}
	
	public void serializeWallInventory(Response msg)
	{
		msg.appendInt32(this.Id);
		msg.appendString(this.getItemInfo().getType().toUpperCase());
		msg.appendInt32(this.Id);
		msg.appendInt32(this.getItemInfo().getSpriteId());

		if (this.getItemInfo().getItemName().contains("a2"))
		{
			msg.appendInt32(3);
		}
		else if (this.getItemInfo().getItemName().contains("wallpaper"))
		{
			msg.appendInt32(2);
		}
		else if (this.getItemInfo().getItemName().contains("landscape"))
		{
			msg.appendInt32(4);
		}
		else
		{
			msg.appendInt32(1);
		}
		msg.appendInt32(0);

		msg.appendString(this.ExtraData);
		
		msg.appendBoolean(this.getItemInfo().getAllowRecycle());
		msg.appendBoolean(this.getItemInfo().getAllowTrade());
		msg.appendBoolean(this.getItemInfo().getAllowInventoryStack());
		msg.appendBoolean(this.getItemInfo().getAllowMarketplaceSell());

		msg.appendInt32(-1);
		msg.appendBoolean(true);
        msg.appendInt32(-1);
	}
}
