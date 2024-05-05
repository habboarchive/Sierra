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

package sierra.messages.incoming.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sierra.Sierra;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.shop.items.ShopItem;
import sierra.habbohotel.shop.items.ShopItemEngine;
import sierra.messages.IMessage;
import sierra.messages.incoming.user.UserCredits;
import sierra.messages.outgoing.item.BoughtItemComposer;
import sierra.messages.outgoing.item.UnseenItemsUpdate;
import sierra.messages.outgoing.item.UpdateInventoryComposer;



public class PurchaseItem extends IMessage
{
	public int PageId;
	public int ItemId;

	@Override
	public void handle()
	{
		try
		{
			ShopItem catalogueItem = ShopItemEngine.shopItems().get(ItemId);

			if (catalogueItem.PageId == PageId)
			{
				if (!session.getHabbo().hasFuse("fuse_mod"))
				{
					if (session.getHabbo().Credits <= catalogueItem.Credits) // Has enough credits
						return;

					session.getHabbo().Credits -= catalogueItem.Credits;

					Sierra.getSocketFactory().getMessageHandler().invokePacket(session, request, UserCredits.class);

					session.getHabbo().save();

				}

				for (int itemid : catalogueItem.Items)
				{
					Furniture furnitureItem = catalogueItem.getItemInfo(itemid);

					session.sendResponse(new BoughtItemComposer(catalogueItem, furnitureItem));

					if (furnitureItem.getItemName().equals("DEAL_HC_1"))
					{
						int days = 31;

						for (int i = 0; i < catalogueItem.Amount; i++)
						{
							session.getSubscription().addOrExtendSubscription(days);
						}

						return;
					}

					Map<Integer, Integer> ItemIds = new HashMap<Integer, Integer>();

					for (int i = 0; i != catalogueItem.Amount; i++)
					{
						PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_inventory (`owner`, `itemid`, `extra_data`) VALUES (?, ?, ?);");
						{
							Statement.setInt(1, session.getHabbo().Id);
							Statement.setInt(2, itemid);
							Statement.setString(3, catalogueItem.Name);
							Statement.executeUpdate();
						}

						ResultSet Keys = Statement.getGeneratedKeys();

						if (Keys.next())
						{
							int id = Keys.getInt(1);

							ItemIds.put(id, furnitureItem.getType().equalsIgnoreCase("s") == true ? 1 : 2);

							session.getInventory().addSingle(id, itemid, catalogueItem.Name);
						}
					}


					session.sendResponse(new UpdateInventoryComposer());
					session.sendResponse(new UnseenItemsUpdate(ItemIds));
					
					ItemIds.clear();
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
