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

import sierra.Sierra;
import sierra.events.Event;
import sierra.events.messenger.PrivateChatEvent;
import sierra.habbo.Session;
import sierra.messages.IMessage;
import sierra.messages.outgoing.messenger.SendFriendMessageComposer;
import sierra.plugin.listeners.ListenerEvent;

public class SendMessage extends IMessage
{
	public int FriendId;
	public String ConsoleMessage;

	@Override
	public void handle()
	{
		Session friend = Sierra.getSocketFactory().getSessionManager().getUserById(this.FriendId);

		if (Sierra.getSocketFactory().getSessionManager().userExists(this.FriendId))
		{
			PrivateChatEvent event = null;
			
			for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.MESSENGER_SEND_MESSAGE))
			{
				event = new PrivateChatEvent(session, friend, ConsoleMessage);

				ListenEvent.Event = event;
				ListenEvent.Listener.onPrivateChatEvent(event);
				
				if (ListenEvent.Event.isCancelled())
					return;
				else
				{
					friend = event.getTo();
					this.ConsoleMessage = event.getMessage();
				}
			}
			
			friend.sendResponse(new SendFriendMessageComposer(this.ConsoleMessage, this.session.getHabbo().Id));
		}
		else
		{
			//session.sendResponse(new SendFriendMessageComposer("Friend i", this.session.getHabbo().Id));
		}
	}
	/*public void logMessage(int FriendId, String Message)
	{
		String Username = 
				Sierra.getServer().getActiveConnections().UserByIdOnline(FriendId) ? 
						Sierra.getServer().getActiveConnections().GetUserById(FriendId).getHabbo().Username :
							Sierra.getDatabase().ReadString("username", "SELECT username FROM members WHERE id = '" + FriendId + "'");

						//Logging.writeLine(Username);
	}*/
}
