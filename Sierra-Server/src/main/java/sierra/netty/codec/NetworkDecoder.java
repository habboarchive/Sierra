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

import java.nio.ByteBuffer;



import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import sierra.netty.readers.Request;


public class NetworkDecoder extends FrameDecoder
{
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
	{
		try 
		{		
			/*
			 * If the received packet has enough units to be parsed
			 */
			if (buffer.readableBytes() < 6)
				return null;

			/*
			 * Read the length of our packet
			 */
			byte[] Length = buffer.readBytes(4).array();

			/*
			 * If the length is the char id of the policy.
			 */
			if (Length[0] == 60)
			{
				buffer.discardReadBytes();

				channel.write("<?xml version=\"1.0\"?>\r\n"
					+ "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
					+ "<cross-domain-policy>\r\n"
					+ "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
					+ "</cross-domain-policy>\0");
			}
			else
			{
				/*
				 * Convert the message length so we know how much to read 8)
				 */
				int MessageLength = ByteBuffer.wrap(Length).asIntBuffer().get();

				/*
				 * Create new channel buffer
				 */
				ChannelBuffer MessageBuffer = buffer.readBytes(MessageLength);

				/*
				 * Get header
				 */
				Short HeaderId = MessageBuffer.readShort();

				/*
				 * Make a new request
				 */
				return new Request(HeaderId, MessageBuffer);
			}		
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		return null;
	}
}