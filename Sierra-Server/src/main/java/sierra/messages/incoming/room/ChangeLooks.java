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

package sierra.messages.incoming.room;

import java.sql.PreparedStatement;

import sierra.Sierra;
import sierra.messages.IMessage;
import sierra.messages.outgoing.room.UpdateLooksComposer;
import sierra.utils.UserInputFilter;



public class ChangeLooks extends IMessage 
{
	public String Sex;
	public String Figure;
	
	@Override
	public void handle()
	{
		/*
		 * Build le packets
		 */
		session.sendRoom(new UpdateLooksComposer(session.getHabbo().Id, Figure, Sex, session.getHabbo().Motto));
		session.sendResponse(new UpdateLooksComposer(-1, Figure, Sex, session.getHabbo().Motto));


		/*
		 * Update SQL
		 */
		try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("UPDATE members SET figure = ?, gender = ? WHERE username = ?");
			{
				Statement.setString(1, Figure);
				Statement.setString(2, Sex.toUpperCase());
				Statement.setString(3, session.getHabbo().Username);
				Statement.execute();

			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		/*
		 * Update Session
		 */
		session.getHabbo().Figure = Figure;
		session.getHabbo().Gender = Sex.toUpperCase();
	}

	/**
	 * @author - matty13
	 */
	public static Boolean Validate(String Figure)
	{
		if (Figure.length() < 18 || Figure.length() > 150)
		{
			return false;
		}

		String[] Sets = Figure.split(".");

		if (Sets.length > 13 || Sets.length < 2)
		{
			return false;
		}

		Boolean HasHD = false;
		Boolean HasLG = false;

		Boolean CheckOthers = (Sets.length > 2);
		Boolean OthersOK = false;

		for (String Set : Sets)
		{
			String[] Parts = Set.split("-");

			if (Parts.length < 3 || Parts.length > 4)
			{
				return false;
			}

			if (Parts[0].length() != 2)
			{
				return false;
			}

			int Type = 0;
			int Colour = 0;

			if (UserInputFilter.isInteger(Parts[1], Type))
			{
				if (Type < 1)
				{
					return false;
				}
			}

			if (UserInputFilter.isInteger(Parts[2], Colour))
			{
				if (Colour < 1)
				{
					return false;
				}
			}

			for (String Part : Parts)
			{
				if (Part.equals("hd"))
				{
					HasHD = true;
				}

				if (Part.equals("lg"))
				{
					HasLG = true;
				}

				if (CheckOthers)
				{
					if (Part.equals("wa") || Part.equals("cc" )|| Part.equals("fa") || Part.equals("ca") || Part.equals("ch") || Part.equals("he") || Part.equals("ea") || Part.equals("cp") || Part.equals("ha") || Part.equals("sh"))
					{
						OthersOK = true;
					}
				}
			}
		}

		if (!HasHD || !HasLG)
		{
			return false;
		}

		if (CheckOthers && !OthersOK)
		{
			return false;
		}

		return true;
	}
}
