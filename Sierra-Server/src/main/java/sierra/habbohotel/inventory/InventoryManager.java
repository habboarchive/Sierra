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

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.messages.outgoing.item.RemoveObjectInventoryComposer;



public class InventoryManager
{
	private ConcurrentLinkedQueue<Inventory> WallItems;
	private ConcurrentLinkedQueue<Inventory> FloorItems;

	public InventoryManager(Session Session)
	{
		FloorItems = new ConcurrentLinkedQueue<Inventory>();
		WallItems = new ConcurrentLinkedQueue<Inventory>();
		
		try
		{
			ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM members_inventory WHERE owner = '" + Session.getHabbo().Id + "'").executeQuery();

			while (Row.next())
			{
				Furniture Furni = FurnitureEngine.get(Row.getInt("itemid"));
				String ExtraData = Row.getString("extra_data");

				if (Furni.getType().equals("s"))
					FloorItems.add(new Inventory(Row.getInt("id"), Row.getInt("itemid"), ExtraData));
				
				if (Furni.getType().equals("i"))
					WallItems.add(new Inventory(Row.getInt("id"), Row.getInt("itemid"), ExtraData));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void dispose()
	{
		this.WallItems.clear();
		this.WallItems = null;
		
		this.FloorItems.clear();
		this.FloorItems = null;
	}
	
	public void addSingle(int Id, int ItemId, String extraData)
	{
		Furniture Furni = FurnitureEngine.get(ItemId);

		if (Furni.getType().equals("s"))
			FloorItems.add(new Inventory(Id, ItemId, extraData));

		if (Furni.getType().equals("i"))
			WallItems.add(new Inventory(Id, ItemId, extraData));
	}
	
	public void removeItem(Session session, Inventory Item, Boolean flag)
	{
		Sierra.getStorage().executeQuery("DELETE FROM `members_inventory` WHERE `id` = '" + Item.getId() + "'");		
		
		session.sendResponse(new RemoveObjectInventoryComposer(Item.getId()));
		
		if (flag)
		{
			this.removeFloorItem(Item);
		}
		else
		{
			this.removeWallItem(Item);
		}
		
		//Sierra.getSocketFactory().getMessageHandler().invokePacket(session, null, OpenInventory.class);
	}
	
	private void removeWallItem(Inventory Item)
	{
		WallItems.remove(Item);
	}
	
	private void removeFloorItem(Inventory Item)
	{
		FloorItems.remove(Item);
	}
	
	public Inventory getWallItem(int Id)
	{
		for (Inventory Item : WallItems)
		{
			if (Item.getId() == Id)
				return Item;
		}
		return null;
	}
	
	public Inventory getFloorItem(int Id)
	{
		for (Inventory Item : FloorItems)
		{
			if (Item.getId() == Id)
				return Item;
		}
		return null;
	}
	

	public Inventory getItem(int Id)
	{
		for (Inventory Item : FloorItems)
		{
			if (Item.getId() == Id)
				return Item;
		}
		
		for (Inventory Item : WallItems)
		{
			if (Item.getId() == Id)
				return Item;
		}
		
		return null;
	}
	
	public ConcurrentLinkedQueue<Inventory> GetWallItems() {
		return WallItems;
	}
	
	public ConcurrentLinkedQueue<Inventory> GetFloorItems() {
		return FloorItems;
	}
}
