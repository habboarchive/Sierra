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

import java.util.logging.Level;
import java.util.logging.Logger;

import sierra.utils.DateTime;


import com.google.common.base.FinalizableReferenceQueue;

public class Log
{
	static void startup() {
		
		writeLine("Sierra - Habbo Hotel Java Server");
		writeLine("Loading version: " + Sierra.FRIENDLY_VERSION);
		writeLine();
		
		Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
		logger.setLevel(Level.OFF);
	}
	
	private static String generateDataFormat()
	{
		return Sierra.getConfiguration().getProperty("date.syntax").replace("{date}", DateTime.now()).replace("{space}", " ");
	}
	
	public static void writeLine()
	{
		System.out.println(generateDataFormat() + "[SIERRA]");
	}
	
	public static void writeLine(Object format) 
	{
		System.out.println(generateDataFormat() + "[SIERRA] >> " + format.toString());
	}
	
	public static void writeTag(String Tag, Object format)
	{
		System.out.println(generateDataFormat() + "[" + Tag + "] >> " + format.toString());
	}
	
	public static void writeBlank(Object format) 
	{
		System.out.println("[SIERRA] >> " + format.toString());
	}
}
