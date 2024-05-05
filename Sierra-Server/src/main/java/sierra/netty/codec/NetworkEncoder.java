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

package sierra.netty.codec;

import java.nio.charset.Charset;



import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import sierra.netty.readers.Response;


public class NetworkEncoder extends SimpleChannelHandler
{
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
	{
		try
		{
			if (e.getMessage() instanceof String)
			{
				Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer(
						(String) e.getMessage(), Charset.forName("UTF-8")));
				
				return;
			}
			if (e.getMessage() instanceof Response)
			{
				Response msg = (Response) e.getMessage();
				Channels.write(ctx, e.getFuture(), msg.get());
				
				return;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
