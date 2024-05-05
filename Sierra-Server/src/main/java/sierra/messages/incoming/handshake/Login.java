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

package sierra.messages.incoming.handshake;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Log;
import sierra.Sierra;
import sierra.events.Event;
import sierra.events.login.LoginEvent;
import sierra.habbo.details.UserInformation;
import sierra.habbohotel.bans.BanEngine;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.RoomEngine;
import sierra.habbohotel.updaters.Updates;
import sierra.messages.IMessage;
import sierra.messages.outgoing.handshake.FuserightComposer;
import sierra.messages.outgoing.handshake.LoginComposer;
import sierra.messages.outgoing.user.CurrencyComposer;
import sierra.messages.outgoing.user.ModToolComposer;
import sierra.plugin.listeners.ListenerEvent;
import sierra.utils.DateTime;



public class Login extends IMessage
{	
	public String SSO;
	
	@Override
	public void handle()
	{
		for (ListenerEvent ListenEvent : Sierra.getPluginManager().getListenerManager().getListenersByEvent(Event.LOGIN))
		{
			LoginEvent Event = new LoginEvent(session, SSO);

			ListenEvent.Event = Event;
			ListenEvent.Listener.onLoginEvent(Event);
			
			if (ListenEvent.Event.isCancelled())
				return;
		}
		
		try
		{
			if (!Sierra.getStorage().entryExists("SELECT * FROM members WHERE ssoticket = '" + SSO + "' LIMIT 1"))
				return;
			
			PreparedStatement statement = Sierra.getStorage().queryParams("SELECT * FROM members WHERE ssoticket = ? LIMIT 1");
			statement.setString(1, SSO);

			ResultSet Row = statement.executeQuery();
			
			Row.next();
			
			UserInformation.setDetails(session, Row);
			
			Sierra.getSocketFactory().getSessionManager().clearDoubles(session.getHabbo().SessionId, session.getHabbo().Id);
			
			if (BanEngine.getBan(session.getIpAddress()) != null)
			{
				session.destroy();
				return;
			}
			
			if (BanEngine.getBan(session.getHabbo().Id) != null)
			{
				if (BanEngine.getBan(session.getHabbo().Id).isExpired() == false)
				{
					session.destroy();
					return;
				}
			}

			RoomEngine.cacheOwnRooms(session.getHabbo().Id);
			
			session.reloadInventory();
			session.reloadSubscription();
			session.reloadMessenger();

			session.sendResponse(new FuserightComposer(session.getSubscription().validSubscription(), session.getHabbo().Rank));
			session.sendResponse(new LoginComposer());
			session.sendResponse(new CurrencyComposer(session.getHabbo().Currencies));

			/*
			 * TODO: Code composer class
			 */
			
			session.getResponse().init(Outgoing.CitizenshipPanel);
            session.getResponse().appendString("citizenship");
            session.getResponse().appendInt32(1);
            session.getResponse().appendInt32(4);
            //session.sendResponse();

			Sierra.getStorage().executeQuery("UPDATE members SET online = '" + 1 + "' WHERE id = '" + session.getHabbo().Id + "'");

			Boolean alertEnabled = Sierra.getConfiguration().getProperty("hotel.alert.enabled").equalsIgnoreCase("true");

			if (session.getHabbo().hasFuse("fuse_mod"))
			{
				session.sendResponse(new ModToolComposer());
			}

			if (alertEnabled)
			{
				String alertType = Sierra.getConfiguration().getProperty("hotel.alert.type");
				String alertMessage = Sierra.getConfiguration().getProperty("hotel.alert.message");

				alertMessage = alertMessage.replace("{nl}", "\n").replace("{username}", session.getHabbo().Username);

				if (alertType.equals("motd"))
				{
					session.sendMotd(alertMessage);
				}
				else
				{
					session.sendNotify(alertMessage);
				}
			}

			if (!Sierra.getStorage().entryExists("SELECT * FROM members_info WHERE user_id = '" + this.session.getHabbo().Id + "'")) 
			{
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_info (`user_id`, `reg_timestamp`, `login_timestamp`, `cfhs`, `cfhs_abusive`, `cautions`, `bans`) VALUES (?, ?, ?, ?, ?, ?, ?);");
				{
					Statement.setInt(1, session.getHabbo().Id);
					Statement.setInt(2, Sierra.getUnixTime());
					Statement.setInt(3, Sierra.getUnixTime());
					Statement.setInt(4, 0);
					Statement.setInt(5, 0);
					Statement.setInt(6, 0);
					Statement.setInt(7, 0);
					Statement.executeUpdate();
				}
				
				Sierra.getStorage().executeQuery("UPDATE members SET last_online = '" +  DateTime.now().toString() + "' WHERE id = '" + this.session.getHabbo().Id + "'");
			}
			else
			{
				Sierra.getStorage().executeQuery("UPDATE members_info SET login_timestamp = '" +  Sierra.getUnixTime() + "' WHERE user_id = '" + this.session.getHabbo().Id + "'");
			}

			session.setAuthenticated(true);

			Sierra.Online++;

			Updates.saveOnlineCount();

			Log.writeLine("The user '" + session.getHabbo().Username + "' has logged in!");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}