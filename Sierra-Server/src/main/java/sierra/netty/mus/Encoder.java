package sierra.netty.mus;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class Encoder extends SimpleChannelHandler {
	
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
	{
		Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer(
				(String) e.getMessage(), Charset.forName("UTF-8")));
	}

}
