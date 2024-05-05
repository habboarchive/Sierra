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

package sierra.habbohotel.commands;

import java.util.ArrayList;
import java.util.List;

import sierra.habbo.Session;



public class CommandEngine 
{
	private static List<Command> Commands;
	
	static {
		Commands = new ArrayList<Command>();
	}
	
	public List<Command> getCommands() {
		return Commands;
	}
	
	public static void loadCommands()
	{
		setCommand("about", "fuse_login").setExecutor(new ICommand());
		setCommand("ban", "fuse_ban").setExecutor(new BanUser());
		setCommand("banip", "fuse_ban").setExecutor(new BanIP());
		setCommand("spam", "fuse_mod").setExecutor(new SpamAlert());
		setCommand("tell", "fuse_login").setExecutor(new TellWisper());
		setCommand(new String[] { "commands", "help" }, "fuse_login").setExecutor(new ViewCommands());
		setCommand(new String[] { "ha", "hotelalert" }, "fuse_mod").setExecutor(new HotelAlert());
		setCommand(new String[] { "nobrain", "rave" }, "fuse_mod").setExecutor(new NobrainHotelDance());
	}
	
	public static List<Command> getCommandsByPermission(Session User)
	{
		List<Command> rankedCommands = new ArrayList<Command>();
		
		for (Command command : Commands)
		{
			if (User.getHabbo().hasFuse(command.getFuseright()));
			{
				rankedCommands.add(command);
			}
		}
		
		return rankedCommands;
	}

	public static Command setCommand(String Label, String fuseright)
	{	
		Command command = new Command(new String[] { Label }, fuseright);
		
		Commands.add(command);
		
		return command;
	}
	
	public static Command setCommand(String[] Label, String fuseright)
	{	
		Command command = new Command(Label, fuseright);
		
		Commands.add(command);
		
		return command;
	}
	
	public static Boolean callCommands(Session sender, String label, String[] args, String Message)
	{
		for (Command command : Commands)
		{
			if (command.isCommand(label) && sender.getHabbo().hasFuse(command.getFuseright()))
			{
				return command.getExecutor().onCommand(sender, label, args, Message);
			}
		}
		return false;
	}
}
