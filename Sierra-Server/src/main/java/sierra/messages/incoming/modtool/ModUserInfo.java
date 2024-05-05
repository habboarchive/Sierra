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

package sierra.messages.incoming.modtool;

import java.sql.ResultSet;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.IMessage;



public class ModUserInfo  extends IMessage 
{
	private int UserId;

	@Override
	public void handle() 
	{
		try 
		{
			Session habbo = new Session(this.UserId);

			if (habbo.getHabbo() == null) {
				return;
			}

			session.getResponse().init(Outgoing.UserTool);
			session.getResponse().appendInt32(habbo.getHabbo().Id);
			session.getResponse().appendString(habbo.getHabbo().Username);
			session.getResponse().appendString(habbo.getHabbo().Figure);

			ResultSet Row = Sierra.getStorage().readRow( "SELECT * FROM members_info WHERE user_id = '" + this.UserId + "'");
			
			session.getResponse().appendInt32(((int)Math.ceil((Sierra.getUnixTime() - Row.getInt("reg_timestamp")) / 60)));
			session.getResponse().appendInt32(((int)Math.ceil((Sierra.getUnixTime() - Row.getInt("login_timestamp")) / 60)));
			
			session.getResponse().appendBoolean(Sierra.getSocketFactory().getSessionManager().userExists(this.UserId));

			session.getResponse().appendInt32(Row.getInt("cfhs"));
			session.getResponse().appendInt32(Row.getInt("cfhs_abusive"));
			session.getResponse().appendInt32(Row.getInt("cautions"));
			session.getResponse().appendInt32(Row.getInt("bans"));
			
			session.getResponse().appendString("");
			session.getResponse().appendInt32(0);
			session.getResponse().appendInt32(0);
			session.getResponse().appendString(habbo.getHabbo().Email);
			//session.sendResponse();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

