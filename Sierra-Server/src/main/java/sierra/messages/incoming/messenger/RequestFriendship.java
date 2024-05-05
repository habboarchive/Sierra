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

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.messenger.requests.FriendRequest;
import sierra.messages.IMessage;
import sierra.messages.outgoing.messenger.FriendRequestComposer;



public class RequestFriendship extends IMessage {

	public String Username;
	
	@Override
	public void handle()
	{
		try
		{
			if (Username == null)
				return;

			Session Asked = Sierra.getSocketFactory().getSessionManager().getUserWithName(Username);

			if (Asked == null)
				return;

			FriendRequest friendRequest = new FriendRequest(session.getHabbo().Id, session.getHabbo().Username, session.getHabbo().Figure);
			
			Asked.getMessenger().addRequest(friendRequest);
			Asked.sendResponse(new FriendRequestComposer(friendRequest));

			if (!Sierra.getStorage().entryExists("SELECT * FROM messenger_requests WHERE to_id = '" + Asked.getHabbo().Id + "' AND from_id = '" + session.getHabbo().Id + "'"))
			{
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO messenger_requests (to_id, from_id) VALUES (?, ?)");
				{
					Statement.setInt(1, Asked.getHabbo().Id);
					Statement.setInt(2, session.getHabbo().Id);
					Statement.execute();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
