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

import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.utils.DateTime;



public class BanUser extends CommandExecutor 
{
	@Override
	public boolean onCommand(Session sender, String label, String[] args, String message) 
	{
		Boolean success = true;

		switch (args.length)
		{
		case 1:
			sender.sendNotify("You haven't supplied the username for this command!");
			success = false;

			break;
		case 2:
			String username = args[1];

			if (Sierra.getStorage().readInt32("SELECT rank FROM members WHERE username = '" + username + "'") < 3)
			{
				String reason = "The ban hammer has spoken!";

				banUser("user", username, sender.getHabbo().Id, reason, Sierra.getUnixTime(), 0, false);

				Session session = Sierra.getSocketFactory().getSessionManager().getUserWithName(username);

				if (session != null)
				{
					session.getRoomUser().getRoom().leaveRoom(true, true, session);
				}
			}
			else
			{
				sender.sendNotify("You cannot ban staff.");
			}
			break;
		case 3:
			username = args[1];

			if (Sierra.getStorage().readInt32("SELECT rank FROM members WHERE username = '" + username + "'") < 3)
			{
				long newtime = parseDateDiff(sender, message.substring(username.length()), true);

				banUser("user", username, sender.getHabbo().Id, "", Sierra.getUnixTime(), (int)newtime, true);

				Calendar calender = DateTime.calendar(newtime);

				sender.sendNotify("The user (" + username + ") won't be unbanned until the \n" + calender.getTime());

				Session session = Sierra.getSocketFactory().getSessionManager().getUserWithName(username);

				if (session != null)
				{
					session.getRoomUser().getRoom().leaveRoom(true, true, session);
				}
			}
			else
			{
				sender.sendNotify("You cannot ban staff.");
			}
			break;
		}

		return success;
	}

	static void banUser(String bantype, String username, int bannedBy, String Reason, int addedDate, int expireDate, boolean appealState)
	{
		try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO bans (`bantype`, `value`, `reason`, `expire_date`, `added_by`, `added_date`, `appeal_state`) VALUES (?, ?, ?, ?, ?, ?, ?)");
			{
				Statement.setString(1, bantype);
				Statement.setString(2, username);
				Statement.setString(3, Reason);
				Statement.setInt(4, expireDate);
				Statement.setInt(5, bannedBy);
				Statement.setInt(6, addedDate);
				Statement.executeUpdate();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * @source Essentials
	 * @HTTP - https://github.com/essentials/Essentials/blob/master/Essentials/src/net/ess3/commands/Commandtempban.java
	 */
	public static long parseDateDiff(Session session, String time, boolean future)
	{
		Pattern timePattern = Pattern.compile(
				"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
						+ "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find())
		{
			if (m.group() == null || m.group().isEmpty())
			{
				continue;
			}
			for (int i = 0; i < m.groupCount(); i++)
			{
				if (m.group(i) != null && !m.group(i).isEmpty())
				{
					found = true;
					break;
				}
			}
			if (found)
			{
				if (m.group(1) != null && !m.group(1).isEmpty())
				{
					years = Integer.parseInt(m.group(1));
				}
				if (m.group(2) != null && !m.group(2).isEmpty())
				{
					months = Integer.parseInt(m.group(2));
				}
				if (m.group(3) != null && !m.group(3).isEmpty())
				{
					weeks = Integer.parseInt(m.group(3));
				}
				if (m.group(4) != null && !m.group(4).isEmpty())
				{
					days = Integer.parseInt(m.group(4));
				}
				if (m.group(5) != null && !m.group(5).isEmpty())
				{
					hours = Integer.parseInt(m.group(5));
				}
				if (m.group(6) != null && !m.group(6).isEmpty())
				{
					minutes = Integer.parseInt(m.group(6));
				}
				if (m.group(7) != null && !m.group(7).isEmpty())
				{
					seconds = Integer.parseInt(m.group(7));
				}
				break;
			}
		}

		if (!found)
		{
			session.sendNotify("That is an illegal date format!");
		}

		Calendar c = new GregorianCalendar();
		if (years > 0)
		{
			c.add(Calendar.YEAR, years * (future ? 1 : -1));
		}
		if (months > 0)
		{
			c.add(Calendar.MONTH, months * (future ? 1 : -1));
		}
		if (weeks > 0)
		{
			c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
		}
		if (days > 0)
		{
			c.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
		}
		if (hours > 0)
		{
			c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
		}
		if (minutes > 0)
		{
			c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
		}
		if (seconds > 0)
		{
			c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
		}
		return c.getTimeInMillis();
	}
}
