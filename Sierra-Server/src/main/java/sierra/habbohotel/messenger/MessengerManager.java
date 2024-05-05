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

package sierra.habbohotel.messenger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.messenger.friends.Friend;
import sierra.habbohotel.messenger.friends.FriendEngine;
import sierra.habbohotel.messenger.requests.FriendRequest;
import sierra.habbohotel.messenger.requests.FriendRequestEngine;
import sierra.netty.readers.Response;



public class MessengerManager
{
	private Session Session;
	private List<Friend> Buddies;
	private List<FriendRequest> Requests;
	private Boolean IsFollowing;

	public MessengerManager(Session User)
	{
		try
		{
			Buddies = FriendEngine.getBuddyList(User.getHabbo().Id);
			Requests = FriendRequestEngine.getFriendRequests(User.getHabbo().Id);

			Session = User;
			IsFollowing = false;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void dispose()
	{
		if (Buddies != null)
			Buddies.clear();
		
		Buddies = null;
		
		if (Requests != null)
			Requests.clear();
		
		Requests = null;
	}

	public List<Friend> getBuddies()
	{
		return Buddies;
	}

	public List<FriendRequest> getFriendRequests()
	{
		return Requests;
	}

	public FriendRequest getFriendRequest(int UserId)
	{
		for (FriendRequest Request : Requests)
		{
			if (Request.UserId == UserId)
			{
				return Request;
			}
		}
		return null;
	}

	public Boolean isBuddy(int UserId)
	{
		for (Friend buddy : Buddies)
		{
			if (buddy.getId() == UserId)
			{
				return true;
			}
		}
		return false;
	}
	
	public void addRequest(FriendRequest Request)
	{
		Requests.add(Request);
	}

	public void addBuddy(int Id)
	{
		List<Friend> Buddies = new ArrayList<Friend>();

		try
		{
			ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM messenger_buddies WHERE friend_id = '" + Id + "' AND user_id = '" + this.Session.getHabbo().Id + "'").executeQuery();

			while (Row.next())
			{
				Buddies.add(new Friend(Row.getInt("friend_id"), Row.getInt("cat_id")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void removeRequest(int senderId)
	{
		Sierra.getStorage().executeQuery("DELETE FROM messenger_requests WHERE from_id = '" + senderId + "' AND to_id = '" + this.Session.getHabbo().Id + "'");
		Sierra.getStorage().executeQuery("DELETE FROM messenger_requests WHERE to_id = '" + senderId + "' AND from_id = '" + this.Session.getHabbo().Id + "'");
	
		this.Requests.remove(this.getFriendRequest(senderId));
	}
	
	public void removeRequests()
	{
		for (FriendRequest friendReq : Requests)
		{
			Sierra.getStorage().executeQuery("DELETE FROM messenger_requests WHERE to_id = '" + friendReq.UserId + "' AND from_id = '" + this.Session.getHabbo().Id + "'");
			Sierra.getStorage().executeQuery("DELETE FROM messenger_requests WHERE from_id = '" + friendReq.UserId + "' AND to_id = '" + this.Session.getHabbo().Id + "'");
		}
		this.Requests.clear();
	}
	
	public void sendStatus(Boolean Online, Boolean InRoom)
	{
		for (Friend Buddy : Session.getMessenger().getBuddies()) {
			Buddy.refreshOnline();
		}

		sendFriends(Session, this.buildUpdateResponse(Session.getResponse(),Session.getHabbo().Id,  Session.getHabbo().Username, Online, InRoom, Session.getHabbo().Figure, Session.getHabbo().Motto));
	}
	
	public void sendOfflineFriend(FriendRequest request, Boolean Online, Boolean InRoom)
	{
		for (Friend Buddy : Session.getMessenger().getBuddies()) {
			Buddy.refreshOnline();
		}

		Session.sendResponse(this.buildUpdateResponse(Session.getResponse(), request.UserId, request.Username, Online, InRoom, request.Look, request.Motto));
	}
	
	public Response buildUpdateResponse(Response Message, int Id, String Username, Boolean Online, Boolean inRoom, String Look, String Motto)
	{
		Session.getResponse().init(Outgoing.UpdateFriendState);
		Session.getResponse().appendInt32(0);
		Session.getResponse().appendInt32(1);
		Session.getResponse().appendInt32(0);
		Session.getResponse().appendInt32(Id);
		Session.getResponse().appendString(Username);
		Session.getResponse().appendInt32(1);
		Session.getResponse().appendBoolean(Online);
		Session.getResponse().appendBoolean(inRoom);
		Session.getResponse().appendString(Look);
		Session.getResponse().appendInt32(0);
		Session.getResponse().appendString(Motto);
		Session.getResponse().appendString("");//fb name
		Session.getResponse().appendString("");
		Session.getResponse().appendBoolean(true);
		Session.getResponse().appendBoolean(true);
		Session.getResponse().appendBoolean(false);
		Session.getResponse().appendBoolean(false);
		Session.getResponse().appendBoolean(false);
		
		return Session.getResponse();
	}
	
	public void sendFriends(Session Session, Response Message)
	{
		for (Friend friend : Session.getMessenger().getBuddies())
		{
			Session friendSession =  Sierra.getSocketFactory().getSessionManager().getUserById(friend.getId());
			
			if (friendSession == null)
			{
				continue;
			}
			
			if (friend.getHabbo() == null)
			{
				continue;
			}
			
			if (friend.getHabbo().Id == Session.getHabbo().Id)
			{
				continue;
			}
			
			friendSession.sendResponse(Message);
		}
	}

	/**
	 * @return the isFollowing
	 */
	public Boolean getIsFollowing() {
		return IsFollowing;
	}

	/**
	 * @param isFollowing the isFollowing to set
	 */
	public void setIsFollowing(Boolean isFollowing) {
		IsFollowing = isFollowing;
	}

}
