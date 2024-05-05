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

package sierra.habbohotel.messenger.requests;

import sierra.Sierra;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;

public class FriendRequest implements ISerialize
{
	public int UserId;
	public String Username;
	public String Look;
	public String Motto;
	
	public FriendRequest(int UserId)
	{
		this.UserId = UserId;
		this.Username = Sierra.getStorage().readString("SELECT username FROM members WHERE id = '" + UserId + "'");
		this.Look = Sierra.getStorage().readString("SELECT figure FROM members WHERE id = '" + UserId + "'");
		this.Motto = Sierra.getStorage().readString("SELECT mission FROM members WHERE id = '" + UserId + "'");
	}
	
	public FriendRequest(int UserId, String Username, String Figure)
	{
		this.UserId = UserId;
		this.Username = Username;
		this.Look = Figure;
	}

	@Override
	public void serialize(Response Message)
	{
		Message.appendInt32(UserId);
		Message.appendString(Username);
		Message.appendString(Look);
	}
}
