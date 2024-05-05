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

import sierra.Sierra;
import sierra.habbo.*;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;


public class UserDataComposer extends ICompose {
	
	private Session Session;

	public UserDataComposer(sierra.habbo.Session session) {
		super();
		this.Session = session;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.UserInfo);
		response.appendInt32(this.Session.getHabbo().Id);
		response.appendString(this.Session.getHabbo().Username);
		response.appendString(this.Session.getHabbo().Figure);
		response.appendString(this.Session.getHabbo().Gender.toUpperCase());
		response.appendString(this.Session.getHabbo().Motto);
		response.appendString(this.Session.getHabbo().Username.toLowerCase());
		response.appendBoolean(true);
		response.appendInt32(8); //8
		response.appendInt32(1); //3
		response.appendInt32(1); //3
		response.appendBoolean(true);
		{
			String lastOnline = Sierra.getStorage().readString("SELECT last_online FROM members WHERE id = '" + this.Session.getHabbo().Id + "' LIMIT 1");

			response.appendString(lastOnline);
		}
		response.appendBoolean(false);
		response.appendBoolean(false);
		return response;
	}

}
