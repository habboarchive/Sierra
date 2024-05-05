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

package sierra.netty.readers;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class Request
{
	private int Header;
	public ChannelBuffer buffer;

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public int getMessageId() {
		return Header;
	}

	public Request(int MessageId, ChannelBuffer buffer)
	{
		this.Header = MessageId;
		this.buffer = (buffer == null || buffer.readableBytes() == 0) ? ChannelBuffers.EMPTY_BUFFER : buffer;
	}

	public Request clone()
	{
		return new Request(this.Header, this.buffer.duplicate());
	}

	public int readShort()
	{
		return buffer.readShort();
	}

	public Integer readInt()
	{
		try
		{
			return buffer.readInt();
		}
		catch (Exception e)
		{
			return 0;
		}
	}


	public boolean readBoolean() 
	{
		try
		{
			return buffer.readByte() == 1;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public String readString()
	{
		try
		{
			int Length = this.readShort();
			byte[] Data = this.buffer.readBytes(Length).array();

			return new String(Data);
		}
		catch (Exception e)
		{
			return "invalid:";
		}
	}

	public String getMessageBody()
	{
		String str = new String(buffer.toString(Charset.defaultCharset()));

		String consoleText = str;

		for (int i = 0; i < 13; i++) { 
			consoleText = consoleText.replace(Character.toString((char)i), "[" + i + "]");
		}

		return consoleText;
	}
}
