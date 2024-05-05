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

package sierra.netty.connections;

import java.sql.PreparedStatement;



import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import sierra.Log;
import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.updaters.Updates;
import sierra.netty.readers.Request;


public class ConnectionHandler extends SimpleChannelHandler
{
	@Override
	public void channelOpen(ChannelHandlerContext ctnx, ChannelStateEvent e)
	{
		if (!Sierra.getSocketFactory().getSessionManager().addSession(ctnx.getChannel()))
		{
			ctnx.getChannel().disconnect();
		}

	} 

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		//Log.writeLine("Disconnection from " + ctx.getChannel().getRemoteAddress().toString().replace("/", "").split(":")[0]);
		try
		{
			Session mSession = (Session)ctx.getChannel().getAttachment();

			if (mSession != null)
			{
				if (mSession.getHabbo() != null)
				{
					if (mSession.hasAuthenticated())
					{
						Sierra.Online--;
						Updates.saveOnlineCount();

						mSession.setAuthenticated(false);
						
						Log.writeLine("The user '" + mSession.getHabbo().Username + "' has logged out!");
					}

					mSession.getMessenger().sendStatus(false, false);
					mSession.getRoomUser().setIsSitting(false);

					PreparedStatement Statement = Sierra.getStorage().queryParams("UPDATE members SET online = ? WHERE id = ?");
					{
						Statement.setInt(1, 0);
						Statement.setInt(2, mSession.getHabbo().Id);
						Statement.execute();
					}

					mSession.getMessenger().dispose();
					mSession.getInventory().dispose();
					mSession.destroy();
				}
			}
		}
		catch (Exception exception2)
		{
		}
		
		Sierra.getSocketFactory().getSessionManager().removeSession(ctx.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		try
		{
			Session session = (Session)ctx.getChannel().getAttachment();

			if (session != null)
			{
				Sierra.getSocketFactory().getMessageHandler().handleRequest(session, (Request)e.getMessage());
			}
			else
			{
				System.out.print("Could not load user session!");
			}
		}
		catch (Exception exception3)
		{
			exception3.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		ctx.getChannel().close();
	}

}
