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

package sierra.habbohotel.wordfilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sierra.Sierra;



public class WordfilterEngine
{
	public static Map<String, String> mWords = new HashMap<String, String>();
	
	public static void load() throws Exception
	{
		PreparedStatement Statement = Sierra.getStorage().queryParams("SELECT * FROM members_wordfilter");
		
		ResultSet Row = Statement.executeQuery();
		
		while (Row.next())
		{
			mWords.put(Row.getString("word"), Row.getString("replace_with"));
		}
	}
	
	public static boolean triggeredFilter(String Phrase)
	{
		for (Entry<String, String> Replace : mWords.entrySet())
		{
			if (Phrase.toLowerCase().contains(Replace.getKey().toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String replaceWith(String Phrase)
	{
		for (Entry<String, String> Replace : mWords.entrySet())
		{
			if (Phrase.toLowerCase().contains(Replace.getKey().toLowerCase()))
			{
				return Phrase.replace(Replace.getKey(), Replace.getValue());
			}
		}
		return "";
	}
}
