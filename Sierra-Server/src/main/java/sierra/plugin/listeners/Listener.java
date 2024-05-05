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

import sierra.events.items.PickUpItemEvent;
import sierra.events.items.PlaceFloorItemEvent;
import sierra.events.items.PlaceWallItemEvent;
import sierra.events.login.CheckReleaseEvent;
import sierra.events.login.LoginEvent;
import sierra.events.messenger.PrivateChatEvent;
import sierra.events.room.*;

public abstract class Listener 
{
	/*
	 * Login
	 */
	public void onLoginEvent(LoginEvent e) { }
	public void onCheckReleaseEvent(CheckReleaseEvent e) { }
	
	/*
	 * Room
	 */
	public void onWalkEvent(WalkEvent e) { }

	/*
	 * Item
	 */
	public void onPickUpItemEvent(PickUpItemEvent e) { }
	public void onPlaceWallItemEvent(PlaceWallItemEvent e) { }
	public void onPlaceFloorItemEvent(PlaceFloorItemEvent e) { }
	
	/*
	 * Room
	 */
	public void onPrivateChatEvent(PrivateChatEvent e) { }
}

