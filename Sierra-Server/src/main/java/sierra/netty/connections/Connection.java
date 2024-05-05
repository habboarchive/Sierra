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

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import sierra.messages.MessageHandler;
import sierra.netty.codec.NetworkDecoder;
import sierra.netty.codec.NetworkEncoder;


public class Connection
{
	private NioServerSocketChannelFactory Factory;
	private ExecutionHandler Execute;
	private ServerBootstrap Bootstrap;

	private MessageHandler Messages;
	private SessionManager Clients;

	private int Port;
	private String Host;

	public Connection(String host, int port)
	{

		this.Factory = new NioServerSocketChannelFactory (
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
						);

		this.Bootstrap = new ServerBootstrap(Factory);
		this.Messages = new MessageHandler();
		this.Clients = new SessionManager();

		this.Host = host;
		this.Port = port;

		configureNetty();
	}

	private void configureNetty()
	{
		this.Execute = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(200, 1048576, 1073741824, 100, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory()));
		
		ChannelPipeline pipeline = this.Bootstrap.getPipeline();

		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler());
		pipeline.addLast("pipelineExecutor", Execute);
	}

	public boolean listenSocket()
	{
		try
		{
			this.Bootstrap.bind(new InetSocketAddress(Host, Port));
		}
		catch (ChannelException ex)
		{
			ex.printStackTrace();
			return false;
		}

		return true;
	}
	
	public MessageHandler getMessageHandler()
	{
		return this.Messages;
	}

	public SessionManager getSessionManager()
	{
		return this.Clients;
	}
	
	public Executor getExecutor()
	{
		return this.Execute.getExecutor();
	}
}
