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

import java.util.Map;
import java.util.Map.Entry;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class UserBadgesComposer extends ICompose {

	private Map<Integer, String> Badges;
	private int UserId;
	
	public UserBadgesComposer(int userId, Map<Integer, String> badges) {
		super();
		this.UserId = userId;
		this.Badges = badges;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.GetUserBadges);
		response.appendInt32(this.UserId);
		response.appendInt32(this.Badges.size());
		
		for (Entry<Integer, String> set : this.Badges.entrySet()) {
			response.appendInt32(set.getKey());
			response.appendString(set.getValue());
		}
		
		response.appendInt32(10);
		response.appendInt32(10);
		response.appendInt32(10);
		return response;
	}

}
