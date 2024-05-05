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

package sierra.messages.incoming.trade;

import sierra.habbo.Session;
import sierra.habbohotel.trade.Trade;
import sierra.messages.IMessage;

public class BeginTrade extends IMessage {

	public int userVirtualId;

	@Override
	public void handle() throws Exception 
	{
		Session tradeUser = session.getRoomUser().getRoom().getUserByVirtualId(userVirtualId);

		if (tradeUser == null)
		{
			return;
		}

		Trade trade = new Trade(session, tradeUser);
		trade.statusUpdate();
		trade.sendWindow();
		
		tradeUser.getRoomUser().setTrade(trade);
		session.getRoomUser().setTrade(trade);
	}
}
