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

package sierra.plugin.listeners;

import java.util.*;

import sierra.events.Event;



public class ListenerManager 
{
	private List<ListenerEvent> RegisteredListeners;

	public ListenerManager()
	{
		this.RegisteredListeners = new ArrayList<ListenerEvent>();
	}

	public void registerListeners(Event events, Listener Listener)
	{
		this.RegisteredListeners.add(new ListenerEvent(null, Listener, events.getId()));
	}

	public List<ListenerEvent> getListenersByEvent(Event events)
	{
		 List<ListenerEvent> Listeners = new ArrayList<ListenerEvent>();
		 
		 for (ListenerEvent listenerEvent : RegisteredListeners)
		 {
			 if (listenerEvent.EventType == events.getId())
			 { 
				 Listeners.add(listenerEvent);
			 }
		 }
		 
		 return Listeners;
	}

	public List<ListenerEvent> getListeners() {
		return RegisteredListeners;
	}
}
