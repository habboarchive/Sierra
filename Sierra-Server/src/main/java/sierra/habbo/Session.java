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

package sierra.habbo;


import org.jboss.netty.channel.Channel;

import sierra.Log;
import sierra.Sierra;
import sierra.habbo.details.UserInformation;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.inventory.InventoryManager;
import sierra.habbohotel.messenger.MessengerManager;
import sierra.habbohotel.room.RoomEngine;
import sierra.habbohotel.room.RoomUser;
import sierra.habbohotel.subscription.Subscription;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;


public class Session
{
	private Boolean LoggedIn;
	private Response mResponse;
	private Channel mChannel;
	private UserInformation mHabbo;
	private Subscription mSubscription;
	private RoomUser mRoomUser;
	private InventoryManager mInventory;
	private MessengerManager mMessenger;

	public Session(Channel Channel) {
		this.mChannel = Channel;
		this.mHabbo = new UserInformation();
		this.mResponse = new Response();
		this.mRoomUser = new RoomUser(this);
		this.LoggedIn = false;
	}

	public Session(String Username) {
		try
		{
			this.mHabbo = new UserInformation();

			UserInformation.setDetails(this, Sierra.getStorage().readRow("SELECT * FROM members WHERE username = '" + Username + "' LIMIT 1"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Session(int Id) {
		try
		{
			this.mHabbo = new UserInformation();

			UserInformation.setDetails(this, Sierra.getStorage().readRow("SELECT * FROM members WHERE id = '" + Id + "' LIMIT 1"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Subscription reloadSubscription() {
		if (mSubscription != null)
		{
			mSubscription.dispose();
			mSubscription = null;
		}
		
		mSubscription = new Subscription(this);
		mSubscription.loadSubscription();

		return mSubscription;
	}
	
	public InventoryManager reloadInventory() {
		if (mInventory != null)
		{
			mInventory.dispose();
			mInventory = null;
		}
		
		mInventory = new InventoryManager(this);

		return mInventory;
	}

	public MessengerManager reloadMessenger() {
		if (mMessenger != null)
		{
			mMessenger.dispose();
			mMessenger = null;
		}
		
		mMessenger = new MessengerManager(this);
		mMessenger.sendStatus(true, this.getRoomUser().getInRoom());
		
		return mMessenger;
	}

	public void destroy() {
		if (mRoomUser.getInRoom())
			mRoomUser.getRoom().leaveRoom(false, true, this);

		RoomEngine.removeByOwnerId(this.getHabbo().Id);

		mHabbo.Currencies.clear();
		mHabbo.GroupMemberships.clear();
		mRoomUser = null;
		
		mMessenger.dispose();
		mInventory.dispose();
		
		mChannel.disconnect();
	}

	public Channel getChannel() {
		return mChannel;
	}

	public UserInformation getHabbo() {
		return mHabbo;
	}

	public InventoryManager getInventory() {
		return mInventory;
	}

	public Subscription getSubscription()  {
		return mSubscription;
	}

	public MessengerManager getMessenger() {
		return mMessenger;
	}

	public RoomUser getRoomUser() {
		return mRoomUser;
	}

	public Response getResponse() {
		return mResponse;
	}

	public String getIpAddress() {
		return mChannel.getRemoteAddress().toString().replace("/", "").split(":")[0];
	}

	public Boolean hasAuthenticated() {
		return LoggedIn;
	}

	public void setAuthenticated(Boolean loggedIn) {
		LoggedIn = loggedIn;
	}
	
	public void sendResponse(ICompose msg)
	{
		Response response = msg.compose();
		
		this.logResponseSent(response);
		mChannel.write(response);
	}


	public void sendResponse(Response msg)
	{
		this.logResponseSent(msg);
		mChannel.write(msg);
	}

	public void sendResponse()
	{
		this.logResponseSent(this.mResponse);
		mChannel.write(mResponse);
	}

	public void sendRoom(Response response)
	{
		if (mRoomUser.getRoom() == null)
			sendResponse(response);
		else
			mRoomUser.getRoom().sendRoom(response);
	}
	
	public void sendRoom(ICompose compose)
	{
		Response response = compose.compose();
		
		if (mRoomUser.getRoom() == null)
			sendResponse(response);
		else
			mRoomUser.getRoom().sendRoom(response);
	}
	
	public void logResponseSent(Response message)
	{
		if (Sierra.getConfiguration().getProperty("log.session.send").equals("1")) {
			if (this.LoggedIn)
				Log.writeLine("(" + this.getHabbo().Username + ") > SENT " + message.getHeader() + " / " + message.getBodyString());
			else
				Log.writeLine("(Unknown) > SENT " + message.getHeader() + " / " + message.getBodyString());

		}
	}
	
	public void sendNotify(String Message)
	{
		this.sendResponse(this.buildNotifyMessage(Outgoing.Alert, Message));
	}

	/*public void sendNotify(String Message, String Link)
	{
		this.sendResponse(this.buildNotifyMessage(Outgoing.Alert, Message, Link));
	}*/

	public void sendMotd(String Message)
	{
		this.getResponse().init(Outgoing.MOTD);
		this.getResponse().appendInt32(1);
		this.getResponse().appendString(Message);
		this.sendResponse();
	}
	
	private Response buildNotifyMessage(int Header, String Message)
	{
		this.getResponse().init(Header);
		this.getResponse().appendString(Message);

		return this.getResponse();
	}

}
