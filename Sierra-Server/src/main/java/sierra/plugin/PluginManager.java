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

package sierra.plugin;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sierra.Log;
import sierra.Sierra;
import sierra.plugin.listeners.ListenerManager;



public class PluginManager 
{
	private String pluginDirectory;

	private ListenerManager listenerManager;

	private List<JavaPlugin> plugins;

	public PluginManager()
	{
		this.plugins = new ArrayList<JavaPlugin>();
		this.listenerManager = new ListenerManager();
	}

	public List<File> pluginFiles(Boolean writeLine)
	{
		List<File> names = new ArrayList<File>();

		if (writeLine)
			Log.writeLine();

		this.pluginDirectory = Sierra.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "plugins" + File.separator;
		this.pluginDirectory = pluginDirectory.replace("Sierra.jar", "");

		try
		{
			for (File file : new File(pluginDirectory).listFiles()) 
			{
				if (file.isFile()) 
				{
					names.add(file);
				}
			}
		}
		catch (NullPointerException e)
		{
			new File(pluginDirectory).mkdir();
			
			//this.pluginFiles(false);
		}

		if (names.size() != 0)
		{
			Log.writeLine("Loading up plugins");
			Log.writeLine();
		}
		
		return names;
	}

	public void loadPlugins() 
	{
		try
		{
			List<File> pluginFiles = this.pluginFiles(true);

			for (File file : pluginFiles)
			{
				URL fileUrl = file.toURI().toURL();
				ClassLoader loader = URLClassLoader.newInstance(
						new URL[] { fileUrl },
						getClass().getClassLoader()
						);

				URL url = new URL("jar:file:" + file.getPath() + "!/plugin.properties");

				DataInputStream stream = new DataInputStream(url.openStream());
				Properties config = new Properties();  
				config.load(stream);  

				Class<?> pluginClass = Class.forName(config.getProperty("plugin.path"), true, loader);
				Class<? extends JavaPlugin> javaPlugin = pluginClass.asSubclass(JavaPlugin.class);

				JavaPlugin Plugin = javaPlugin.newInstance();

				Plugin.PluginName = config.getProperty("plugin.name");
				Plugin.PluginAuthor = config.getProperty("plugin.author");
				Plugin.PluginVersion = config.getProperty("plugin.version");

				plugins.add(Plugin);
			}
		}
		catch (MalformedURLException e) 	{
			e.printStackTrace();
		} catch (IOException e) 			{
			e.printStackTrace();
		} catch (InstantiationException e) 	{
			e.printStackTrace();
		} catch (IllegalAccessException e) 	{
			e.printStackTrace();
		} catch (ClassNotFoundException e) 	{
			e.printStackTrace();
		}
	}

	public void preparePlugins() 
	{
		for (JavaPlugin Plugin : plugins)
		{
			Plugin.Log("Loading v" + Plugin.PluginVersion);
			Plugin.onEnable();
		}
		
		if (plugins.size() != 0)
			Log.writeLine();
	}

	public ListenerManager getListenerManager() {
		return listenerManager;
	}

}
