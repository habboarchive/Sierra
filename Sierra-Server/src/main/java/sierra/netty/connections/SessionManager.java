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

package sierra.netty.connections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.jboss.netty.channel.Channel;

import sierra.habbo.Session;
import sierra.netty.readers.Response;


public class SessionManager
{
	private ConcurrentMap<Integer, Session> Sessions;

	public SessionManager()
	{
		Sessions = new ConcurrentHashMap<Integer, Session>();
	}

	public ConcurrentMap<Integer, Session> getSessions()
	{
		return Sessions;
	}

	public boolean hasSession(Channel channel)
	{
		return Sessions.containsKey(channel.getId());
	}

	public boolean addSession(Channel channel)
	{
		Session session = new Session(channel);

		channel.setAttachment(session);

		return Sessions.putIfAbsent(channel.getId(), session) == null;
	}

	public void removeSession(Channel channel)
	{
		try {
			Sessions.remove(channel.getId());
		} catch (Exception e) {
		}
	}

	public void sendMassMassage(Response response) 
	{
		for (Session Session : Sessions.values())
		{
			if (Session.hasAuthenticated())
			{
				Session.sendResponse(response);
			}
		}
	}

	public void sendMotd(String msg) 
	{
		for (Session Session : Sessions.values())
		{
			if (Session.hasAuthenticated())
			{
				Session.sendMotd(msg);
			}
		}
	}

	public void sendNotify(String msg) 
	{
		for (Session Session : Sessions.values())
		{
			if (Session.hasAuthenticated())
			{
				Session.sendNotify(msg);
			}
		}
	}

	public Session getUserByChannel(Channel channel)
	{
		return (Session)channel.getAttachment();
	}


	public boolean userExists(int id)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Id == id)
					return true;
		return false;
	}

	public Session getUserWithName(String Name)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Username != null)
					if (Session.getHabbo().Username.toLowerCase().equals(Name.toLowerCase()))
						return Session;
		return null;
	}

	public void clearDoubles(int sessionId, int userId)
	{
		for (Session session : Sessions.values())
		{
			if (session.getHabbo() != null)
			{
				if (session.getHabbo().SessionId != sessionId && session.getHabbo().Id == userId)
				{
					session.destroy();
				}
			}
		}
	}

	public Session getUserById(int UserId)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Id == UserId)
					return Session;
		return null;
	}
}
