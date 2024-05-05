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

package sierra.habbohotel.furniture.interactions;

import java.util.AbstractList;
import java.util.List;
import java.util.Random;

import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.items.floor.FloorItem;



public class VendingMachineAction implements Interaction {

	@Override
	public void Interact(Boolean State, Session Session, FloorItem Item)
	{
		if (Item.ExtraData.equals("1"))
		{
			int index = getRandomInt(Item.getItemInfo().getVendingIds());

			if (index == 0)
				Interact(null, Session, Item);

			Session.getResponse().init(Outgoing.ItemInHand);
			Session.getResponse().appendInt32(Session.getHabbo().Id);
			Session.getResponse().appendInt32(index);
			Session.sendRoom(Session.getResponse());
		}

		Item.changeExtraData();
		Item.sendUpdate(Session);
		Item.saveExtraData();
	}	

	public static int getRandomInt(List<Integer> list)
	{ 
		int vendingId = 0;
		int counter = 0;

		int index = new Random().nextInt(list.size());

		for (Integer vendingid : list)
		{
			counter++;

			if (counter == index)
				vendingId = vendingid;
		}
		return vendingId;
	}

	public static int[] convertStringArraytoIntArray(String[] sarray) 
	{
		int intarray[] = new int[sarray.length];

		for (int i = 0; i < sarray.length; i++)
		{
			intarray[i] = Integer.parseInt(sarray[i].replace(" ", ""));
		}

		return intarray;
	}

	public static List<Integer> asList(final int[] is)
	{
		return new AbstractList<Integer>()
		{
			public Integer get(int i) 
			{ 
				return is[i];
			}
			public int size()
			{ 
				return is.length;
			}
		};
	}
}