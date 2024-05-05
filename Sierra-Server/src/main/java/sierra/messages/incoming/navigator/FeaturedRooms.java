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

package sierra.messages.incoming.navigator;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbohotel.navigator.cats.CategoryEngine;
import sierra.habbohotel.room.Room;
import sierra.messages.IMessage;
import sierra.messages.outgoing.navigator.NavigatorFeaturedComposer;
import sierra.messages.outgoing.navigator.NavigatorListComposer;



public class FeaturedRooms extends IMessage
{
	@Override
	public void handle()
	{
		if (CategoryEngine.getRoomSize() == 0) {
			session.sendResponse(new NavigatorListComposer(new ConcurrentLinkedQueue<Room>()));
			return;
		}
		
		session.sendResponse(new NavigatorFeaturedComposer(CategoryEngine.getRoomCategories()));
	}
}
