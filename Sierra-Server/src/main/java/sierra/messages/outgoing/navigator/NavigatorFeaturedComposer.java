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

package sierra.messages.outgoing.navigator;

import java.util.List;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.navigator.cats.RoomCategory;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class NavigatorFeaturedComposer extends ICompose {

	private List<RoomCategory> Cats;
	
	public NavigatorFeaturedComposer(List<RoomCategory> cats) {
		super();
		this.Cats = cats;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.Featured);
		response.appendInt32(Cats.size());

		for (RoomCategory Cat : Cats) {
			Cat.serialize(this.response);
		}

		response.appendInt32(0);
		response.appendInt32(0);
		return response;
	}

}
