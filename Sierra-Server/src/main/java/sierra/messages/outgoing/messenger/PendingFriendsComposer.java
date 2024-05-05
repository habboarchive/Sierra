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

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.messenger.requests.FriendRequest;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class PendingFriendsComposer extends ICompose {

	private List<FriendRequest> pendingFriends;
	
	public PendingFriendsComposer(List<FriendRequest> pendingFriends) {
		super();
		this.pendingFriends = pendingFriends;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.PendingFriends);
		response.appendInt32(this.pendingFriends.size());
		response.appendInt32(this.pendingFriends.size());
		
		for (FriendRequest Request : this.pendingFriends) {
			response.appendBody(Request);
		}
		
		return response;
	}

}
