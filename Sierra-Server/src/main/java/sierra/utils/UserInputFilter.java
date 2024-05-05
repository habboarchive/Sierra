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

package sierra.utils;

public class UserInputFilter
{
	public static String filterString(String str, boolean allowLineBreaks) {
		
		str = str.replace((char) 1, ' ');
		str = str.replace((char) 2, ' ');
		str = str.replace((char) 3, ' ');
		str = str.replace((char) 9, ' ');
		
		if (!allowLineBreaks) {
			str = str.replace((char) 10, ' ');
			str = str.replace((char) 13, ' ');
		}
		
		return str;
	}
	
	public static Boolean isNullOrEmpty(String str)
	{
		if (str == null)
			return true;
		else if (str.length() == 0)
			return true;
		else
			return false;
	}
	
	public static Boolean isNullOrEmpty(Integer str)
	{
		if (str == null)
			return true;
		else if (str == -999999)
			return true;
		else
			return false;
	}
	
	public static Boolean isInteger(String str, final int i)
	{
		try
		{
			Integer.parseInt(str, i);
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
