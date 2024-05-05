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

package sierra.messages.outgoing.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sierra.Log;
import sierra.Sierra;
import sierra.habbo.details.UserInformation;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class UserProfileComposer extends ICompose {

	private int Id;
	private UserInformation Session;

	public UserProfileComposer(int id, UserInformation session) {
		super();
		this.Id = id;
		this.Session = session;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.LoadProfile);
		response.appendInt32(this.Session.Id);
		response.appendString(this.Session.Username);
		response.appendString(this.Session.Figure);
		response.appendString(this.Session.Motto);
		response.appendString("1-1-1969");
		response.appendInt32(0);			// Achievement Score
		response.appendInt32(getShortFriendList(this.Session.Id).size()); 		// Friend Count
		response.appendBoolean(getShortFriendList(this.Session.Id).contains(this.Id)); 	// Is friend
		response.appendBoolean(false); 	// Friend request sent
		response.appendBoolean(Sierra.getSocketFactory().getSessionManager().userExists(this.Session.Id)); 	// Is online		// ?
		response.appendInt32(this.Session.GroupMemberships.size());

		Log.writeLine(this.Session.GroupMemberships.size());
		
		try
		{
			for (int id : this.Session.GroupMemberships)
			{
				
				
				ResultSet row = Sierra.getStorage().readRow("SELECT * FROM groups WHERE id = '" + id + "'");
				response.appendInt32(id);
				response.appendString(row.getString("name"));
				response.appendString(row.getString("image"));
				response.appendString(row.getString("html_color1"));
				response.appendString(row.getString("html_color2"));
				response.appendBoolean(false);//id == Session.GetHabbo().FavoriteGuild);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		response.appendInt32(0); 
		response.appendBoolean(true); 
		return response;
	}

	public List<Integer> getShortFriendList(int UserId)
	{
		List<Integer> Friends = new ArrayList<Integer>();
		
		try
		{
			ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM messenger_buddies WHERE user_id = '" + UserId + "'").executeQuery();
			
			while (Row.next())
			{
				Friends.add(Row.getInt("friend_id"));
			}
		}
		catch (Exception e)
		{
		}
		return Friends;
	}
}
