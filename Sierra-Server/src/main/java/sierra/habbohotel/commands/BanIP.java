package sierra.habbohotel.commands;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sierra.Sierra;
import sierra.habbo.Session;



public class BanIP extends CommandExecutor {

	@Override
	public boolean onCommand(Session sender, String label, String[] args, String message) {

		Boolean success = true;

		switch (args.length)
		{
		case 1:
			sender.sendNotify("You haven't supplied the username for this command!");
			success = false;

			break;
		case 2:
			String ip = args[1];

			if (Sierra.getStorage().readInt32("SELECT rank FROM members WHERE ip = '" + ip + "'") < 3)
			{
				String reason = "The ban hammer has spoken!";

				BanUser.banUser("user", ip, sender.getHabbo().Id, reason, Sierra.getUnixTime(), 0, false);

				Session session = Sierra.getSocketFactory().getSessionManager().getUserWithName(ip);

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
			ip = args[1];

			if (this.checkIPv4(ip))
			{

				if (Sierra.getStorage().readInt32("SELECT rank FROM members WHERE username = '" + ip + "'") < 3)
				{
					String reason = "The ban hammer has spoken!";

					long newtime = BanUser.parseDateDiff(sender, message.substring(ip.length()), true);

					BanUser.banUser("ip", ip, sender.getHabbo().Id, reason, Sierra.getUnixTime(), (int) newtime, false);

					Session session = Sierra.getSocketFactory().getSessionManager().getUserWithName(ip);

					if (session != null)
					{
						session.getRoomUser().getRoom().leaveRoom(true, true, session);
					}
				}
				else
				{
					sender.sendNotify("You cannot ban staff.");
				}
			}
			else
			{
				sender.sendNotify("Not a valid IP address!");
			}
			break;
		}
		return success;
	}

	public boolean checkIPv4(final String ip) {
		boolean isIPv4;
		try {
			final InetAddress inet = InetAddress.getByName(ip);
			isIPv4 = inet.getHostAddress().equals(ip)
					&& inet instanceof Inet4Address;
		} catch (final UnknownHostException e) {
			isIPv4 = false;
		}
		return isIPv4;
	}

}
