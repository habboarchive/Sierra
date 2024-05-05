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

package sierra.habbo.details;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sierra.Sierra;
import sierra.habbo.*;
import sierra.habbohotel.fuserights.FuserightEngine;



public class UserInformation
{
	public int Id;
	public int SessionId;
	public int Rank;
	public String Username;
	public String Email;
	public String Gender;
	public String Figure;
	public String Motto;
	public int Credits;
	public int Pixels;
	public int Snowflakes;
	public boolean Quiz;
	public boolean GroupColours;
	public Map<Integer, Integer> Currencies;
	public List<Integer> GroupMemberships;

	public static void setDetails(Session session, ResultSet Row) throws Exception
	{
		Random random = new Random();
		
		session.getHabbo().Id = Row.getInt("id");
		session.getHabbo().SessionId = random.nextInt(10000);
		session.getHabbo().Rank = Row.getInt("rank");
		session.getHabbo().Username = Row.getString("username");
		session.getHabbo().Figure = Row.getString("figure");
		session.getHabbo().Motto = Row.getString("mission");
		session.getHabbo().Gender = Row.getString("gender");
		session.getHabbo().Credits = Row.getInt("credits");
		session.getHabbo().Pixels = Row.getInt("pixels");
		session.getHabbo().Email = Row.getString("email");
		session.getHabbo().Quiz = Row.getBoolean("safety_chat");
		session.getHabbo().GroupColours = false;
		session.getHabbo().Currencies = new HashMap<Integer, Integer>();
		
		session.getHabbo().Currencies.put(0, session.getHabbo().Pixels);
		session.getHabbo().modifyPixelAmount(session.getHabbo().Pixels, false);
		
		session.getHabbo().GroupMemberships = new ArrayList<Integer>();
		
		ResultSet row = Sierra.getStorage().queryParams("SELECT * FROM `members_group_memberships` WHERE user_id = '" + session.getHabbo().Id + "'").executeQuery();

		while (row.next())
		{
			session.getHabbo().GroupMemberships.add(row.getInt("group_id"));
		}
	}

	public Boolean hasFuse(String Fuse)
	{
		return FuserightEngine.getFusesrightsByRank(Rank).contains(Fuse);
	}

	public Boolean hasRank(Rank rank)
	{
		return Rank == rank.getRank();
	}

	public void modifyPixelAmount(int amount, Boolean add)
	{
		if (add)
			Pixels += amount;
		else
			Pixels = amount;

		this.save();

		Currencies.put(1, Pixels);
	}

	public void save()
	{	
		try
		{
			PreparedStatement update = Sierra.getStorage().queryParams("UPDATE members SET rank = ?, gender = ?, figure = ?, mission = ?, credits = ?, safety_chat = ? WHERE username = ? LIMIT 1");
			
			update.setInt(1, this.Rank);
			update.setString(2, this.Gender);
			update.setString(3, this.Figure);
			update.setString(4, this.Motto);
			update.setInt(5, this.Credits);
			update.setInt(6, (this.Quiz ? 1 : 0));
			update.setString(7, this.Username);
			update.execute();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
