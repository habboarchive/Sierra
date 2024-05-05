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

package sierra.habbohotel.messenger.categories;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;



public class MessengerCatEngine
{
	private static List<MessengerCat> Categories;
	
	static {
		Categories = new ArrayList<MessengerCat>();
	}

	public static void load() throws Exception
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM messenger_categories;").executeQuery();

		while (Row.next())
		{
			Categories.add(new MessengerCat(Row.getInt("id"), Row.getString("label"), Row.getInt("min_rank")));
		}
	}
	
	public static List<MessengerCat> getCategories(int MinRank)
	{
		List<MessengerCat> Own = new ArrayList<MessengerCat>();

		for (MessengerCat OwnCat : Categories)
		{
			if (OwnCat.MinRank == MinRank || OwnCat.MinRank < MinRank)
			{
				Own.add(OwnCat);
			}
		}
		return Own;
	}
	public static int getId(String Label)
	{
		for (MessengerCat Cat : Categories)
		{
			if (Cat.Label.toLowerCase().contains(Label.toLowerCase()))
				return Cat.Id;
		}
		return 0;
	}
}
