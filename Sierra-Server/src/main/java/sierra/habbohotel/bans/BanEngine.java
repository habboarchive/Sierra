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

package sierra.habbohotel.bans;

import java.sql.ResultSet;
import java.sql.SQLException;

import sierra.Sierra;



public class BanEngine 
{
	public static Ban getBan(Object Value)
	{
		try
		{
			ResultSet Row = Sierra.getStorage().readRow("SELECT * FROM bans WHERE value = '" + Value + "' LIMIT 1");
			
			if (Row != null)
				return new Ban(Integer.valueOf(Row.getString("value")), Row.getInt("added_by"), Row.getLong("added_date"), Row.getLong("expire_date"), Row.getBoolean("appeal_state"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
