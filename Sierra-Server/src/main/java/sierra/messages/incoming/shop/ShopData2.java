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

package sierra.messages.incoming.shop;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.IMessage;

public class ShopData2 extends IMessage
{
	@Override
	public void handle()
	{
        session.getResponse().init(Outgoing.ShopData2);
        session.getResponse().appendBoolean(true);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(10);
        
        for (int i = 3372; i < 3382; i++) {
            session.getResponse().appendInt32(i);
        }
        
        session.getResponse().appendInt32(7);
        session.getResponse().appendInt32(0);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(2);
        session.getResponse().appendInt32(3);
        session.getResponse().appendInt32(4);
        session.getResponse().appendInt32(5);
        session.getResponse().appendInt32(6);
        session.getResponse().appendInt32(11);
        session.getResponse().appendInt32(0);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(2);
        session.getResponse().appendInt32(3);
        session.getResponse().appendInt32(4);
        session.getResponse().appendInt32(5);
        session.getResponse().appendInt32(6);
        session.getResponse().appendInt32(7);
        session.getResponse().appendInt32(8);
        session.getResponse().appendInt32(9);
        session.getResponse().appendInt32(10);
        session.getResponse().appendInt32(7);
        
        for (int i = 187; i < 194; i++) {
            session.getResponse().appendInt32(i);
        }
        
        //session.sendResponse();
        
        session.getResponse().init(Outgoing.Offer);
        session.getResponse().appendInt32(100);
        session.getResponse().appendInt32(6);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(2);
        session.getResponse().appendInt32(40);
        session.getResponse().appendInt32(0x63);
        //session.sendResponse();
	}
}