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

package sierra.messages.outgoing.messenger;

import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.messenger.friends.Friend;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class LoadFriendsComposer extends ICompose {

	private List<Friend> Buddies;

	public LoadFriendsComposer(List<Friend> buddies) {
		super();
		this.Buddies = buddies;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.LoadFriends);
		response.appendInt32(1100);
		response.appendInt32(300);
		response.appendInt32(800);
		response.appendInt32(1100);
		response.appendInt32(0);

		response.appendInt32(Buddies.size());

		for (Friend friend : Buddies)
		{
			friend.refreshOnline();
			
			response.appendInt32(friend.getHabbo().Id);
			response.appendString(friend.getHabbo().Username);
			response.appendInt32(1);
			response.appendBoolean(friend.getOnline());
			response.appendBoolean(friend.getOnline() ? (Sierra.getSocketFactory().getSessionManager().getUserById(friend.getHabbo().Id).getRoomUser() != null ? Sierra.getSocketFactory().getSessionManager().getUserById(friend.getHabbo().Id).getRoomUser().getInRoom() : false) : false);
			response.appendString(friend.getHabbo().Figure);
			response.appendInt32(0);
			response.appendString(friend.getHabbo().Motto);
			response.appendString("");// Facebook name
			response.appendString("");
			response.appendBoolean(true);
			response.appendBoolean(true);
			response.appendBoolean(false);
			response.appendBoolean(false);
			response.appendBoolean(false);
		}
		return response;
	}

}
