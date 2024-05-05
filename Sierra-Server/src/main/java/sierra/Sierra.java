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

package sierra;

import java.io.FileInputStream;
import java.util.Properties;

import sierra.boot.CommandLine;
import sierra.habbohotel.HabboHotel;
import sierra.habbohotel.commands.CommandEngine;
import sierra.netty.connections.Connection;
import sierra.netty.mus.MusListener;
import sierra.plugin.PluginManager;
import sierra.storage.Storage;



public class Sierra extends HabboHotel
{
	public static int Online;
	static String FRIENDLY_VERSION;
	
    public static void main(String[] args)
	{	
    	Online = 0;
		FRIENDLY_VERSION = "0.0";
		
		try
		{
			Boolean IsWorking = true;
			
			config = new Properties();  
			config.load(new FileInputStream("sierra.properties"));
			
			if (IsWorking)
			{
				Log.startup();
				
				Log.writeLine("Connecting to MySQL...");
				
				mysql = new Storage(config);
				
				if (mysql.getSQLException())
				{
					Log.writeLine("MySQL failed to connect, please check your settings!");
				}
				else
				{
					Log.writeLine("MySQL connection successful!");
					Log.writeLine();
					Log.writeLine("Aaron is a fag!");

					if (Sierra.generateHotelData())
					{
						CommandEngine.loadCommands();
						
						pluginManager = new PluginManager();
						pluginManager.loadPlugins();
						pluginManager.preparePlugins();

						Log.writeLine("Loading Socket Factory...");

						Integer port = Integer.parseInt(config.getProperty("game.port"));

						connection = new Connection(config.getProperty("game.host"), port);

						if (connection.listenSocket())
						{	
							Sierra.getStorage().queryParams("UPDATE members SET online = 0").execute();
							Sierra.getStorage().queryParams("UPDATE `system` SET `value` = '0' WHERE `id` = 'online_count'").execute();

							musListener = new MusListener();
							musListener.Listen();
							
							CommandLine.start();
							
							Log.writeLine("Server started on port " + port);
							Log.writeLine();
						
						}
						else
						{ 
							Log.writeLine("Unable to listen on port: " +  port);
						}
					}
					else
					{
						Log.writeLine("Unable to successfully start Sierra. Shutting down!");
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.writeBlank("Exception when booting up Sierra: " + e.getMessage());
			e.printStackTrace();
		}
	}

    public static int getUnixTime()
    {
    	return (int) (System.currentTimeMillis() / 1000L);
    }
    
}
