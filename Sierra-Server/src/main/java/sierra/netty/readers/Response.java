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

import java.io.IOException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;

public class Response
{
	private int Id;
	private ChannelBufferOutputStream bodystream;
	private ChannelBuffer body;
	
	public Response() {
		
	}
	
	public Response(int id) 
	{
		this.Id = id;
		this.body = ChannelBuffers.dynamicBuffer();
		this.bodystream = new ChannelBufferOutputStream(body);

		try {
			this.bodystream.writeInt(0);
			this.bodystream.writeShort(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Response init(int id)
	{
		this.Id = id;
		this.body = ChannelBuffers.dynamicBuffer();
		this.bodystream = new ChannelBufferOutputStream(body);

		try {
			this.bodystream.writeInt(0);
			this.bodystream.writeShort(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void appendString(Object obj)
	{
		try {
			bodystream.writeUTF(obj.toString());
		} catch (IOException e) {
		}
	}
	
	public void appendInt32(Integer obj)
	{
		try {
			bodystream.writeInt(obj);
		} catch (IOException e) {
		}
	}
	
	public void appendInt32(Boolean obj)
	{
		try {
			bodystream.writeInt(obj ? 1 : 0);
		} catch (IOException e) {
		}
	}
	
	public void appendShort(int obj)
	{
		try {
			bodystream.writeShort((short)obj);
		} catch (IOException e) {
		}
	}

	public void appendBoolean(Boolean obj)
	{
		try {
			bodystream.writeBoolean(obj);
		} catch (IOException e) {
		}
	}
	
	public void appendBody(ISerialize obj)
	{
		try {
			obj.serialize(this);
		} catch (Exception e) {
		}
	}
	
	public void AppendResponse(Response obj)
	{
		try {
			this.bodystream.write(obj.body.array());
		} catch (Exception e) {
		}
	}
	
	public String getBodyString()
	{
		ChannelBuffer bodeh = body.duplicate();
		
		bodeh.setInt(0, bodeh.writerIndex() - 4);
		
		String str = new String(bodeh.toString(Charset.defaultCharset()));

		String consoleText = str;

		for (int i = 0; i < 14; i++) { 
			consoleText = consoleText.replace(Character.toString((char)i), "[" + i + "]");
		}

		return consoleText;
	}
	
	public int getHeader() {

		return Id;
	}
	
	public ChannelBuffer get()
	{
		body.setInt(0, body.writerIndex() - 4);
		
		return this.body;
	}
}
