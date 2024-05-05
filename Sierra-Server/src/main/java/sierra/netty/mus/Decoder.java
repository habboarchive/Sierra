package sierra.netty.mus;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class Decoder extends FrameDecoder {
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
	{
		try 
		{
			if (buffer.readableBytes() < 0)
				return null;
			
			String Data = buffer.readBytes(buffer.readableBytes()).toString(Charset.defaultCharset());
			
			return Data;
		}
		catch (Exception e)
		{
		}
		return null;
	}
}
