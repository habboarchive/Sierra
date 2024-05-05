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

package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.*;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;


public class RoomEditDataComposer extends ICompose {

	private Room Room;

	public RoomEditDataComposer(sierra.habbohotel.room.Room room) {
		super();
		this.Room = room;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.GetRoomData);
		response.appendInt32(this.Room.getId());
		response.appendString(this.Room.getName());
		response.appendString(this.Room.getDescription());
		response.appendInt32(this.Room.getState());
		response.appendInt32(this.Room.getCategory());
		response.appendInt32(this.Room.getMaximumInRoom());
		response.appendInt32(this.Room.getMaximumInRoom());
		response.appendInt32(this.Room.getTags().size());

		for (String tag : this.Room.getTags()) {
			response.appendString(tag);
		}

		response.appendInt32(this.Room.getRights().size());
		response.appendInt32(this.Room.getAllowPets() ? 1 : 0);
		response.appendInt32(this.Room.getAllowPetsEat() ? 1 : 0);
		response.appendInt32(this.Room.getAllowWalkthrough() ? 1 : 0);
		response.appendInt32(this.Room.getHideWall() ? 1 : 0);
		response.appendInt32(this.Room.getWallThickness());
		response.appendInt32(this.Room.getFloorThickness());
		response.appendInt32(0);
		response.appendInt32(0);
		response.appendInt32(0);
		return response;
	}

}
