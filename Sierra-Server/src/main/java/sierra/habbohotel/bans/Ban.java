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

package sierra.habbohotel.bans;

import sierra.Sierra;

public class Ban 
{
	private int UserId;
	private long AddedDate;
	private long ExpireDate;
	private boolean Appeal;
	
	public Ban(int UserId, int BannedBy, long AddedDate, long ExpireDate, boolean Appeal)
	{
		this.UserId = UserId;
		this.AddedDate = AddedDate;
		this.ExpireDate = ExpireDate;
		this.Appeal = Appeal;
	}
	
	public boolean isExpired()
	{
		if (this.ExpireDate <= Sierra.getUnixTime())
		{
			return false;
		}

		return true;
	}
	
	public int getUserId() 
	{
		return this.UserId;
	}
	
	public long getAddedDate()
	{
		return this.AddedDate;
	}
	
	public long getExpireDate()
	{
		return this.ExpireDate;
	}
	
	public boolean appealState()
	{
		return this.Appeal;
	}
}
