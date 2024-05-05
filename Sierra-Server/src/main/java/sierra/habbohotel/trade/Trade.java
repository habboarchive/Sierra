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

package sierra.habbohotel.trade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.inventory.Inventory;
import sierra.netty.readers.Response;



public class Trade {

	private Session TradeFrom;
	private Session TradeTo;

	public Map<Session, TradeUser> TradeUsers;
	public Map<Session, List<Inventory>> OfferedItems;

	public Trade(Session tradeFrom, Session tradeTo)
	{
		this.TradeFrom = tradeFrom;
		this.TradeTo = tradeTo;

		this.TradeUsers = new HashMap<Session, TradeUser>();
		this.TradeUsers.put(this.TradeFrom, new TradeUser());
		this.TradeUsers.put(this.TradeTo, new TradeUser());

		this.OfferedItems = new HashMap<Session, List<Inventory>>();
		this.OfferedItems.put(this.TradeFrom, new ArrayList<Inventory>());
		this.OfferedItems.put(this.TradeTo, new ArrayList<Inventory>());
	}

	public void statusUpdate()
	{
		if (!TradeFrom.getRoomUser().Statuses.containsKey("trd"))
		{
			TradeFrom.getRoomUser().Statuses.put("trd", "");
			TradeFrom.getRoomUser().updateStatus();

			TradeFrom.getRoomUser().setTrading(true);
		}

		if (!TradeTo.getRoomUser().Statuses.containsKey("trd"))
		{
			TradeTo.getRoomUser().Statuses.put("trd", "");
			TradeTo.getRoomUser().updateStatus();

			TradeTo.getRoomUser().setTrading(true);
		}
	}

	public void clearStatus()
	{
		if (TradeFrom.getRoomUser().Statuses.containsKey("trd"))
		{
			TradeFrom.getRoomUser().Statuses.remove("trd");
			TradeFrom.getRoomUser().updateStatus();

			TradeTo.getRoomUser().setTrading(false);
		}

		if (TradeTo.getRoomUser().Statuses.containsKey("trd"))
		{
			TradeTo.getRoomUser().Statuses.remove("trd");
			TradeTo.getRoomUser().updateStatus();

			TradeTo.getRoomUser().setTrading(false);
		}
	}

	public void sendWindow()
	{
		Response message = new Response(Outgoing.TradeStart);
		message.appendInt32(this.TradeFrom.getHabbo().Id);
		message.appendInt32(1);
		message.appendInt32(this.TradeTo.getHabbo().Id);
		message.appendInt32(1);

		sendTraders(message);
	}

	public void closeTrade(int userId) {

		Response message = new Response(Outgoing.TradeClose);
		message.appendInt32(userId);
		message.appendInt32(2);

		sendTraders(message);

		this.clearStatus();

		this.OfferedItems.clear();
	}
	
	public void closeTradeClean(int userId) {

		Response message = new Response(Outgoing.TradeCloseClean);
		message.appendInt32(userId);
		message.appendInt32(1);

		sendTraders(message);
		
		message = new Response(Outgoing.UpdateInventory);
		sendTraders(message);
		
		this.clearStatus();

		this.TradeUsers.clear();
		this.OfferedItems.clear();
	}

	public void acceptTrade(Session session) {

		this.TradeUsers.get(session).setAccepted(true);

		Response message = new Response(Outgoing.TradeAcceptUpdate);
		message.appendInt32(session.getHabbo().Id);
		message.appendInt32(1);

		sendTraders(message);

		if (this.allUsersAccepted())
		{
			message = new Response(Outgoing.TradeComplete);
			sendTraders(message);
		}
	}


	public void unaccept(Session session) 
	{
		this.TradeUsers.get(session).setAccepted(false);

		Response message = new Response(Outgoing.TradeAcceptUpdate);
		message.appendInt32(session.getHabbo().Id);
		message.appendInt32(0);

		sendTraders(message);
	}

	public void reloadItems()
	{
		Response message = new Response(Outgoing.TradeUpdate);
		for (Entry<Session, List<Inventory>> set : this.OfferedItems.entrySet())
		{
			message.appendInt32(set.getKey().getHabbo().Id);
			message.appendInt32(set.getValue().size());

			for (Inventory item : set.getValue())
			{
				message.appendInt32(item.getId());
				message.appendString(item.getItemInfo().getType().toLowerCase());
				message.appendInt32(item.getId());
				message.appendInt32(item.getItemInfo().getSpriteId());
				message.appendInt32(0);
				message.appendBoolean(true);
				message.appendInt32(0);
				message.appendString("");
				message.appendInt32(0);
				message.appendInt32(0);
				message.appendInt32(0);
				if (item.getItemInfo().getType().toLowerCase().equals("s"))
				{
					message.appendInt32(0);
				}
			}
		}
		sendTraders(message);
	}

	public void redeemItems(Session session) {

		try
		{
			Response message = new Response(Outgoing.TradeAcceptUpdate);
			message.appendInt32(session.getHabbo().Id);
			message.appendInt32(1);
			sendTraders(message);

			if (this.allUsersAccepted())
			{
				for (Entry<Session, List<Inventory>> set : this.OfferedItems.entrySet())
				{
					for (Inventory item : set.getValue())
					{
						if (set.getKey().getHabbo().Id == this.TradeFrom.getHabbo().Id)
						{
							insertItem(item, this.TradeTo, this.TradeFrom);
						}
					}
					
					for (Inventory item : set.getValue())
					{
						if (set.getKey().getHabbo().Id == this.TradeTo.getHabbo().Id)
						{
							insertItem(item, this.TradeFrom, this.TradeTo);
						}
					}
				}
			}
			
			this.closeTradeClean(session.getHabbo().Id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void insertItem(Inventory item, Session user, Session from) throws SQLException
	{
		from.getInventory().removeItem(from, item, item.getItemInfo().getType().equals("s"));
		
		PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_inventory (`owner`, `itemid`, `extra_data`) VALUES (?, ?, ?);");
		{
			Statement.setInt(1, user.getHabbo().Id);
			Statement.setInt(2, item.getItemInfo().getId());
			Statement.setString(3, item.getItemName());
			Statement.executeUpdate();
			
			ResultSet Keys = Statement.getGeneratedKeys();
			
			if (Keys.next())
			{
				int id = Keys.getInt(1);

				user.getInventory().addSingle(id, item.getItemInfo().getId(), item.getItemName());
			}
		}
	}
	
	public Boolean allUsersAccepted()
	{
		return (this.TradeUsers.get(this.TradeFrom).isAccepted() && this.TradeUsers.get(this.TradeTo).isAccepted());
	}

	public void sendTraders(Response message)
	{
		TradeTo.sendResponse(message);
		TradeFrom.sendResponse(message);
	}
}
