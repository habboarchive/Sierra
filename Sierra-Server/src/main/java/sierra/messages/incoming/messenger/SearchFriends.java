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

package sierra.messages.incoming.messenger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.messenger.search.MessengerSearch;
import sierra.messages.IMessage;
import sierra.messages.outgoing.messenger.SearchFriendsComposer;



public class SearchFriends extends IMessage
{
	public String SearchHabbo;

	@Override
	public void handle() throws Exception
	{
		List<MessengerSearch> Friends = new ArrayList<MessengerSearch>();
		List<MessengerSearch> Strangers = new ArrayList<MessengerSearch>();
		List<Integer> SearchIds = new ArrayList<Integer>();

		PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT id FROM members WHERE username LIKE ?");
		{
			Statement.setString(1, SearchHabbo + "%");
		}

		ResultSet searchRow = Statement.executeQuery();

		while (searchRow.next())
			SearchIds.add(searchRow.getInt("id"));

		for (Integer Id : SearchIds) 
		{
			ResultSet Row = Sierra.getStorage().readRow("SELECT * FROM members WHERE id = '" +  Id + "'");

			if (IsBuddy(session, Id))
			{
				Friends.add(new MessengerSearch(Id , Row.getString("username"), Row.getString("mission"), IsOnline(Id), Row.getString("figure")));
			}
			else
			{
				Strangers.add(new MessengerSearch(Id, Row.getString("username"), Row.getString("mission"), IsOnline(Id), Row.getString("figure")));
			}
		}

		session.sendResponse(new SearchFriendsComposer(Friends, Strangers));

		Friends.clear();
		Strangers.clear();
	}

	private Boolean IsOnline(Integer Id) {
		return Sierra.getSocketFactory().getSessionManager().getUserById(Id) != null;
	}

	private Boolean IsBuddy(Session Session, Integer Id) {
		return Session.getMessenger().isBuddy(Id);
	}
}
