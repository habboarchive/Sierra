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

package sierra.habbohotel.messenger.requests;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;





public class FriendRequestEngine
{
	public static List<FriendRequest> getFriendRequests(int UserId) throws Exception
	{
		List<FriendRequest> Requests = new ArrayList<FriendRequest>();

		PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM messenger_requests WHERE to_id = ?");
		{
			Statement.setInt(1, UserId);
		}

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			Requests.add(new FriendRequest(Row.getInt("from_id")));
		}
		return Requests;
	}
}
