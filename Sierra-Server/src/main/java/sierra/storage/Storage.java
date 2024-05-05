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

package sierra.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.SQLException;

import sierra.Log;


import com.jolbox.bonecp.BoneCP;

public class Storage
{
	private Boolean SQLException;

	private BoneCP boneCP;

	private Connection driverConnection;
	private Statement driverStatement;

	public Storage(Properties config)
	{	
		this.SQLException = false;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");

			StoragePooling Pooling = new StoragePooling();

			if (!Pooling.getStoragePooling(config))
			{
				this.SQLException = true;
				return;
			}
			else
			{
				this.boneCP = Pooling.getBoneCP();
				this.driverConnection = this.boneCP.getConnection();
				this.driverStatement = this.driverConnection.createStatement();
			}

		}
		catch (ClassNotFoundException exception1) 
		{
			Log.writeLine("Could not find JDBC class!");
		} 
		catch (SQLException exception2)
		{
			this.SQLException = true;
		}
		catch (Exception e)
		{
			//Log.writeLine("Caught unhandled exception.");
		}
	}

	public String readString(String query) 
	{	
		try 
		{
			ResultSet result = driverStatement.executeQuery(query);
			result.first();
			return result.getString(query.split(" ")[1]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Integer readInt32(String query)
	{	
		try
		{
			ResultSet result = driverStatement.executeQuery(query);
			result.first();
			return result.getInt(query.split(" ")[1]);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		return 0;
	}

	public PreparedStatement queryParams(String query)
	{
		try
		{
			return driverConnection.prepareStatement(query);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public void executeQuery(String query)
	{
		try
		{
			driverStatement.execute(query);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean entryExists(String query)
	{
		try 
		{
			ResultSet result = driverStatement.executeQuery(query);
			return result.next();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public Integer entryCount(String q)
	{
		int i = 0;

		try 
		{
			ResultSet resSet = driverStatement.executeQuery(q);

			while (resSet.next())
			{
				++i;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return i;
	}

	public Integer entryCount(PreparedStatement pStmt)
	{
		int i = 0;

		try 
		{
			ResultSet resSet = pStmt.executeQuery();

			while (resSet.next())
			{
				++i;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return i;
	}

	public ResultSet readRow(String Query)
	{
		try 
		{
			ResultSet resSet = driverStatement.executeQuery(Query);

			while (resSet.next()) 
			{
				return resSet;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public Boolean getSQLException()
	{
		return SQLException;
	}

	public void setSQLException(Boolean flag)
	{
		this.SQLException = flag;
	}
}