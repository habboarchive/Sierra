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

import java.io.File;

import sierra.Log;
import sierra.Sierra;



public abstract class JavaPlugin 
{
	public String PluginName;
	public String PluginAuthor;
	public String PluginVersion;
	
	public abstract void onEnable();
	public abstract void onDisable();
	
	public void Log(String Info)
	{
		Log.writeTag(PluginName, Info);
	}
	
	public String getPluginDirectory() {
		return Sierra.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "plugins" + File.separator + PluginName + File.separator;
	}
}
