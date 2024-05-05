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

package sierra.habbohotel.fuserights;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sierra.Sierra;



public class FuserightEngine
{
	private static Map<Integer, List<String>> Fuserights;
	
	static {
		Fuserights = new HashMap<Integer, List<String>>();
	}

	public static void load() throws Exception
	{
		PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM `fuserights`");

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			Integer Rank = Row.getInt("rank");

			if (Fuserights.containsKey(Rank) == false)
			{
				Fuserights.put(Rank, fillFuses(Rank));
			}
		}
	}
	
	public static ArrayList<String> fillFuses(int Rank)
	{
		ArrayList<String> Fuses = new ArrayList<String>();
		
		try
		{
			ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM fuserights WHERE rank = '" + Rank + "'").executeQuery();

			while (Row.next())
			{
				Fuses.add(Row.getString("fuse"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return Fuses;
	}

	public static List<String> getFusesrightsByRank(int Rank)
	{
		List<String> LoadedFuses = new ArrayList<String>();

		for (Entry<Integer, List<String>> KeyValue : Fuserights.entrySet())
		{
			if (KeyValue.getKey().equals(Rank) || KeyValue.getKey() < Rank)
			{
				for (String fuse : KeyValue.getValue())
				{
					LoadedFuses.add(fuse);
				}
			}
		}
		return LoadedFuses;
	}
}
