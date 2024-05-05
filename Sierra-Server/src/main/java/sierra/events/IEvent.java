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

package sierra.events;

import sierra.habbo.Session;

public class IEvent 
{
	protected Boolean Cancel;
	protected Boolean AlteredCancel;
	protected Session Session;
	
	public IEvent(Session session)
	{
		this.Cancel = false;
		this.AlteredCancel = false;
		this.Session = session;
	}
	
	public void setCancelled(Boolean Cancel) {
		this.Cancel = Cancel;
		this.AlteredCancel = true;
	}
	
	public Boolean isCancelled() {
		return AlteredCancel ? Cancel : false;
	}
	
	public Session getSession() {
		return Session;
	}
}
