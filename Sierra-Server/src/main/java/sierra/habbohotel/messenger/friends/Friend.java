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

package sierra.habbohotel.messenger.friends;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbo.details.UserInformation;

public class Friend
{
	private int mId;
	private int mCategoryId;
	
	private Boolean mOnline;
	private UserInformation mHabbo;

	public Friend(int UserId, int CategoryId)
	{
		this.mId = UserId;
		this.mCategoryId = CategoryId;
		this.mOnline = Sierra.getSocketFactory().getSessionManager().userExists(getId());
		this.mHabbo = new Session(mId).getHabbo();

	}
	
	public Boolean refreshOnline()
	{
		this.mOnline = Sierra.getSocketFactory().getSessionManager().userExists(getId());
		
		return mOnline;
	}

	public int getId() {
		return mId;
	}

	public int getCategoryId() {
		return mCategoryId;
	}
	
	public Boolean getOnline()  {
		return Sierra.getSocketFactory().getSessionManager().userExists(mId);
	}
	
	public UserInformation getHabbo()
	{
		return mHabbo;
	}
}
