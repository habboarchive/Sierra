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
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.messenger.requests.FriendRequest;
import sierra.messages.IMessage;



public class AcceptFriendship extends IMessage
{
	public int Amount;
	
	private List<FriendRequest> Requests;
	
	@Override
	public void handle() throws Exception 
	{
		this.Requests = new ArrayList<FriendRequest>();
		
		for (int i = 0; i < Amount; i++)
		{
			int senderId = request.readInt();
			
			this.Requests.add(session.getMessenger().getFriendRequest(senderId));
		}

		try
		{
			for (FriendRequest Friend : Requests)
			{
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO messenger_buddies (friend_id, user_id, cat_id) VALUES (?, ?, ?)");
				{
					Statement.setInt(1, Friend.UserId);
					Statement.setInt(2, session.getHabbo().Id);
					Statement.setInt(3, 0);
					Statement.execute();
				}
				
				Statement = Sierra.getStorage().queryParams("INSERT INTO messenger_buddies (user_id, friend_id, cat_id) VALUES (?, ?, ?)");
				{
					Statement.setInt(1, Friend.UserId);
					Statement.setInt(2, session.getHabbo().Id);
					Statement.setInt(3, 0);
					Statement.execute();
				}
				
				Sierra.getStorage().queryParams("DELETE FROM messenger_requests WHERE from_id = '" + Friend.UserId + "' AND to_id = '" + session.getHabbo().Id + "';").execute();
				Sierra.getStorage().queryParams("DELETE FROM messenger_requests WHERE to_id = '" + Friend.UserId + "' AND from_id = '" + session.getHabbo().Id + "';").execute();
				
				Session sessionFriend = Sierra.getSocketFactory().getSessionManager().getUserById(Friend.UserId);
				
				if (sessionFriend != null)
				{
					sessionFriend.reloadMessenger();
					sessionFriend.getMessenger().sendStatus(true, sessionFriend.getRoomUser().getInRoom());
				}
				else
				{
					session.getMessenger().sendOfflineFriend(Friend, false, false);
				}
				
				session.reloadMessenger();
				session.getMessenger().sendStatus(true, session.getRoomUser().getInRoom());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}