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

package sierra.habbohotel.messenger.search;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;

public class MessengerSearch implements ISerialize
{
	private Integer HabboId;
    private String Look;
    private String Motto;
    private Boolean Online;
    public String Username;
    
    public MessengerSearch(Integer Id, String Name, String Motto, Boolean Online, String Look) {
    	this.HabboId = Id;
    	this.Username = Name;
    	this.Motto = Motto;
    	this.Online = Online;
    	this.Look = Look;
    }
    
	private Session getSession(Integer HabboId) {
		return Sierra.getSocketFactory().getSessionManager().getUserById(HabboId);
	}
	
	private Boolean inRoom(Integer HabboId){
		return getSession(HabboId).getRoomUser().getInRoom();
	}
	
	@Override
	public void serialize(Response Message) {
        Message.appendInt32(HabboId);
        Message.appendString(Username);
        Message.appendString(Online ? Motto : new String());
        Message.appendBoolean(Online ? inRoom(HabboId) : false);
        Message.appendBoolean(Online);
        Message.appendString("");
        Message.appendInt32(1);
        Message.appendString(Look);
        Message.appendString("1-1-1970");
    }
}
