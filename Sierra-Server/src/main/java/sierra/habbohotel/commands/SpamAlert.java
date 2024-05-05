package sierra.habbohotel.commands;

import sierra.Sierra;
import sierra.habbo.Session;

public class SpamAlert extends CommandExecutor {

	@Override
	public boolean onCommand(Session sender, String label, String[] args, String message) 
	{
		try
		{
			for (Session session : Sierra.getSocketFactory().getSessionManager().getSessions().values())
			{
				for (int i = 0; i < Integer.valueOf(args[1]); i++)
				{
					session.sendNotify(BuildString(2, args));
				}
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public String BuildString(int StartFrom, String[] args)
	{
		String Output = "";
		
		int i = 0;
		for (String arg : args)
		{
			if (args[0] != arg && (i > StartFrom  || i == StartFrom))
			{
				Output += arg + " ";
			}
			i++;
		}
		
		return Output;
	}
}
