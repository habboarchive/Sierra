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

package sierra.habbohotel.allowances;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.netty.readers.Response;



public class AllowanceEngine 
{
	private static List<Allowance> Allowances;

	static {
		Allowances = new ArrayList<Allowance>();
	}

	public static void load()
	{
		try
		{
			ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM `allowances`").executeQuery();

			while (Row.next())
			{
				String label = Row.getString("label");
				String level = Row.getString("level");
				String fieldPerm = Row.getString("field_permission");
				int minrank = Row.getInt("min_rank");
				boolean override = Row.getBoolean("override");
				boolean overridestate = Row.getBoolean("override_state");

				Allowances.add(new Allowance(label, level, fieldPerm, minrank, override, overridestate));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static HashMap<Allowance, Boolean> getAllowances(Session session)
	{
		HashMap<Allowance, Boolean> allowances = new HashMap<Allowance, Boolean>();

		try
		{
			for (Allowance allow : Allowances)
			{		
				boolean hasPermission = true;

				if (allow.Override)
				{
					hasPermission = allow.OverrideState;
				}
				else
				{
					if (allow.MinRank == session.getHabbo().Rank || session.getHabbo().Rank > allow.MinRank)
					{
						if (allow.FieldPermission.length() != 0)
						{
							hasPermission = session.getHabbo().getClass().getField(allow.FieldPermission).getBoolean(session.getHabbo());
						}
					}
					else
					{
						hasPermission = false;
					}
				}
				
				allowances.put(allow, hasPermission);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return allowances;
	}

	public static Response composse(Session session)
	{
		HashMap<Allowance, Boolean> allowances = AllowanceEngine.getAllowances(session);

		Response Message = new Response(Outgoing.Allowances);	
		Message.appendInt32(allowances.size());

		for (Entry<Allowance, Boolean> set : allowances.entrySet())
		{
			set.getKey().compose(set.getValue(), Message);
		}

		return Message;
	}
}
