package sierra.netty.mus;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.Executors;


import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import sierra.Log;
import sierra.Sierra;


public class MusListener 
{	
	private NioServerSocketChannelFactory Factory;
	private ServerBootstrap Bootstrap;
	private ChannelPipeline Pipeline;
	
	public MusListener() 
	{
		this.Factory = new NioServerSocketChannelFactory
		(
			Executors.newCachedThreadPool(),
			Executors.newCachedThreadPool()
		);
		
		this.Bootstrap = new ServerBootstrap(this.Factory);
		
		this.Pipeline = this.Bootstrap.getPipeline();
		this.Pipeline.addLast("encoder", new Encoder());
		this.Pipeline.addLast("decoder", new Decoder());
		this.Pipeline.addLast("handler", new MusHandler());
	}
	
	public void Listen() throws SQLException
	{
		int Port = Integer.valueOf(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)).substring(0, 5)) / 2;
		
		this.Bootstrap.bind(new InetSocketAddress(Port));
		
		Sierra.getStorage().queryParams("UPDATE `system` SET `value` = '" + Port + "' WHERE `id` = 'mus_port'").execute();
		
		Log.writeLine("MUS binded on port " + Port);
	}

}
