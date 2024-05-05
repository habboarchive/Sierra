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

package sierra.habbohotel.threading;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory implements ThreadFactory
{
	private int mPriority;
	private String mName;
	private ThreadGroup mGroup;
	
	public PriorityThreadFactory(String Name, int Priority)
	{
		this.mPriority = Priority;
		this.mName = Name;
		this.mGroup = new ThreadGroup(this.mName);
	}

	@Override
	public Thread newThread(Runnable r)
	{
		Thread thread = new Thread(mGroup, r);
		thread.setName(mName);
		thread.setPriority(mPriority);
		return thread;
	}
}
