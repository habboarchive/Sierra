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

public class ShopData1 extends IMessage
{
	@Override
	public void handle()
	{
        session.getResponse().init(Outgoing.ShopData1);
        session.getResponse().appendBoolean(true);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(0);
        session.getResponse().appendInt32(0);
        session.getResponse().appendInt32(1);
        session.getResponse().appendInt32(0x2710);
        session.getResponse().appendInt32(0x30);
        session.getResponse().appendInt32(7);
        //session.sendResponse();
	}
}
