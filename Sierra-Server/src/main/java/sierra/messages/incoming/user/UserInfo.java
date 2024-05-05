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

package sierra.messages.incoming.user;

import sierra.Sierra;
import sierra.messages.IMessage;
import sierra.messages.incoming.messenger.AllFriends;
import sierra.messages.outgoing.user.AllowanceComposer;
import sierra.messages.outgoing.user.UserDataComposer;
import sierra.messages.outgoing.user.WelcomeUserComposer;
import sierra.utils.DateTime;

public class UserInfo extends IMessage 
{
	@Override
	public void handle()
	{
		session.sendResponse(new UserDataComposer(session));
		session.sendResponse(new WelcomeUserComposer());
		session.sendResponse(new AllowanceComposer(session));

		Sierra.getStorage().executeQuery("UPDATE members SET last_online = '" +  DateTime.now().toString() + "' WHERE id = '" + this.session.getHabbo().Id + "'");
		
		Sierra.getSocketFactory().getMessageHandler().invokePacket(session, request, UserCredits.class);
		Sierra.getSocketFactory().getMessageHandler().invokePacket(session, request, AllFriends.class);
	}
}
