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

package sierra.habbohotel.navigator.cats;

import sierra.habbohotel.room.Room;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;

public class RoomCategory implements ISerialize 
{
	public int ImageSize;
	public String Picture;
	public Room RoomInfo; 
	
	public String Label;
	public String Description;

	public RoomCategory(int ImageSize, String Picture, String Label, String Description, Room Info)
	{
		this.ImageSize = ImageSize;
		this.Picture = Picture;
		this.RoomInfo = Info;
		this.Label = Label;
		this.Description = Description;
	}

	public void serialize(Response Message)
	{
		Message.appendInt32(RoomInfo.getId());
		Message.appendString(Label);
		Message.appendString(Description);
		Message.appendInt32(this.ImageSize); 
		Message.appendString(Label);
		Message.appendString(Picture);
		Message.appendInt32(0);
		Message.appendInt32(RoomInfo.getUsers().size());
		Message.appendInt32(2);
		
		RoomInfo.serialize(Message);
	}
}
