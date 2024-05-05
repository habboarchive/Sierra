package sierra.netty.mus;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import sierra.Sierra;


public class MusHandler extends SimpleChannelHandler 
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) 
	{
		try
		{
			String message = (String)e.getMessage();

			String command = message.split(Character.toString((char)1))[0];

			if (message.substring(command.length()).length() > 2)
			{
				message = message.substring(command.length() + 1);
			}
			
			if (command.equals("ha"))
			{
				Sierra.getSocketFactory().getSessionManager().sendNotify(message);
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

	}

}
