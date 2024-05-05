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

package sierra.habbohotel.room.items.wall;

import sierra.Sierra;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;

public class WallItem implements ISerialize
{
	public int Id;
	public int BaseId;
	public String Position;
	public String ExtraData;

	public WallItem(int Id, int BaseId, String Position, String ExtraData)
	{
		this.Id = Id;
		this.BaseId = BaseId;
		this.Position = Position;
		this.ExtraData = ExtraData;
	}
	
	public Furniture getItemInfo() 
	{
		return FurnitureEngine.get(BaseId);
	}
	
	public void serialize(Response msg)
	{
		msg.appendString(Id);
		msg.appendInt32(getItemInfo().getSpriteId());
		msg.appendString(Position);
		msg.appendString(ExtraData);
		msg.appendInt32(getItemInfo().getInteractionType().equals("default") ? 1 : 0);
		msg.appendInt32(-1);
		msg.appendInt32(-1);
	}

	public void changeExtraData()
	{
		if (ExtraData.isEmpty())
		{
			ExtraData = "1";
		}
		else
		{
			if (ExtraData.contains("1"))
				ExtraData = "0";
			else if (ExtraData.contains("0"))
				ExtraData = "1";
		}
	}

	public void saveExtraData()
	{
		try 
		{
			Sierra.getStorage().queryParams("UPDATE room_items SET extra = '" + ExtraData + "' WHERE id = '" + Id + "';").execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
