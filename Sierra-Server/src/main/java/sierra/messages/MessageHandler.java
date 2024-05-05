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

package sierra.messages;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import sierra.Log;
import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Incoming;
import sierra.messages.incoming.groups.BuyGroup;
import sierra.messages.incoming.groups.BuyGroupDialog;
import sierra.messages.incoming.groups.GroupInfo;
import sierra.messages.incoming.handshake.*;
import sierra.messages.incoming.item.AddPapers;
import sierra.messages.incoming.item.ChangeFloorItemState;
import sierra.messages.incoming.item.ChangeWallItemState;
import sierra.messages.incoming.item.ExchangeCoin;
import sierra.messages.incoming.item.MoveFloorItem;
import sierra.messages.incoming.item.MoveWallItem;
import sierra.messages.incoming.item.OpenInventory;
import sierra.messages.incoming.item.PickUpItem;
import sierra.messages.incoming.item.PlaceItem;
import sierra.messages.incoming.item.PurchaseGift;
import sierra.messages.incoming.item.PurchaseItem;
import sierra.messages.incoming.item.RollDice;
import sierra.messages.incoming.messenger.AcceptFriendship;
import sierra.messages.incoming.messenger.DenyFriendship;
import sierra.messages.incoming.messenger.FollowFriend;
import sierra.messages.incoming.messenger.FollowRoomInfo;
import sierra.messages.incoming.messenger.InviteFriends;
import sierra.messages.incoming.messenger.RequestFriendship;
import sierra.messages.incoming.messenger.SearchFriends;
import sierra.messages.incoming.messenger.SendMessage;
import sierra.messages.incoming.modtool.ModGetRoom;
import sierra.messages.incoming.modtool.ModGetRoomChatlog;
import sierra.messages.incoming.modtool.ModUserInfo;
import sierra.messages.incoming.navigator.CanCreateRoom;
import sierra.messages.incoming.navigator.CreateRoom;
import sierra.messages.incoming.navigator.FeaturedRooms;
import sierra.messages.incoming.navigator.LeaveRoom;
import sierra.messages.incoming.navigator.OwnRooms;
import sierra.messages.incoming.navigator.PopularRooms;
import sierra.messages.incoming.navigator.SearchRooms;
import sierra.messages.incoming.room.AnswerQuiz;
import sierra.messages.incoming.room.ApplyDance;
import sierra.messages.incoming.room.ApplySign;
import sierra.messages.incoming.room.ChangeLooks;
import sierra.messages.incoming.room.Chat;
import sierra.messages.incoming.room.ClickHabbo;
import sierra.messages.incoming.room.DeleteRoom;
import sierra.messages.incoming.room.GiveRights;
import sierra.messages.incoming.room.InitalizeRoom;
import sierra.messages.incoming.room.KickUser;
import sierra.messages.incoming.room.LoadHeightmap;
import sierra.messages.incoming.room.RemoveAllRights;
import sierra.messages.incoming.room.RemoveRights;
import sierra.messages.incoming.room.RoomExternalData;
import sierra.messages.incoming.room.RoomSettings;
import sierra.messages.incoming.room.Shout;
import sierra.messages.incoming.room.StartQuiz;
import sierra.messages.incoming.room.StartTyping;
import sierra.messages.incoming.room.StopTyping;
import sierra.messages.incoming.room.Walk;
import sierra.messages.incoming.room.Wave;
import sierra.messages.incoming.room.Wisper;
import sierra.messages.incoming.shop.ClubPackages;
import sierra.messages.incoming.shop.ShopCategories;
import sierra.messages.incoming.shop.ShopData1;
import sierra.messages.incoming.shop.ShopData2;
import sierra.messages.incoming.shop.ShopPages;
import sierra.messages.incoming.trade.AcceptTrade;
import sierra.messages.incoming.trade.BeginTrade;
import sierra.messages.incoming.trade.CancelTrade;
import sierra.messages.incoming.trade.ConfirmTrade;
import sierra.messages.incoming.trade.OfferItem;
import sierra.messages.incoming.trade.UnacceptTrade;
import sierra.messages.incoming.user.ClubStatus;
import sierra.messages.incoming.user.UserInfo;
import sierra.messages.incoming.user.UserProfile;
import sierra.messages.outgoing.groups.ManageGroup;
import sierra.netty.readers.Request;
import sierra.utils.UserInputFilter;



public class MessageHandler
{
	private Map<Integer, Class<? extends IMessage>> mMessages;

	public MessageHandler()
	{
		this.mMessages = new HashMap<Integer, Class<? extends IMessage>>();

		this.registerHandshake();
		this.registerUsers();
		this.registerCatalogue();
		this.registerNavigator();
		this.registerRooms();
		this.registerTrade();
		this.registerItems();
		this.registerSnowstorm();
		this.registerGroups();
		this.registerMessenger();
		this.registerModTool();
	}

	public boolean isExisting(int id)
	{
		return mMessages.containsKey((short)id);
	}

	public Class<? extends IMessage> getMessageBy(Short id)
	{
		return mMessages.get(id);
	}

	public void registerHandshake()
	{
		mMessages.put(Incoming.CheckRelease, CheckRelease.class);
		mMessages.put(1266, InitCrypto.class);
		mMessages.put(Incoming.GenerateSecretKey, GenerateSecretKey.class);
		mMessages.put(Incoming.LogIn, Login.class);
	}

	public void registerGroups()
	{
		mMessages.put(Incoming.GroupInfo, GroupInfo.class);
		mMessages.put(Incoming.BuyGroupDialog, BuyGroupDialog.class);
		mMessages.put(Incoming.BuyGroup, BuyGroup.class);
		mMessages.put(Incoming.ManageGroup, ManageGroup.class);
	}

	public void registerUsers()
	{
		mMessages.put(Incoming.GetProfile, UserProfile.class);
		mMessages.put(Incoming.UserInformation, UserInfo.class);
		mMessages.put(Incoming.ClubStatus, ClubStatus.class);
	}

	public void registerCatalogue()
	{
		mMessages.put(Incoming.GetCataIndex, ShopCategories.class);
		mMessages.put(Incoming.GetCataPage, ShopPages.class);
		mMessages.put(Incoming.CatalogData1, ShopData1.class);
		mMessages.put(Incoming.CatalogData2, ShopData2.class);
		mMessages.put(Incoming.HabboClubPackages, ClubPackages.class);
	}

	public void registerMessenger()
	{
		mMessages.put(Incoming.SearchFriends, SearchFriends.class);
		mMessages.put(Incoming.PrivateChat, SendMessage.class);
		mMessages.put(Incoming.RequestFriendship, RequestFriendship.class);
		mMessages.put(Incoming.AcceptFriendship, AcceptFriendship.class);
		mMessages.put(Incoming.DenyFriendship, DenyFriendship.class);
		mMessages.put(Incoming.FollowFriend, FollowFriend.class);
		mMessages.put(Incoming.FollowRoomInfo, FollowRoomInfo.class);
		mMessages.put(Incoming.InviteFriends, InviteFriends.class);
	}

	public void registerNavigator()
	{
		mMessages.put(Incoming.FeaturedRooms, FeaturedRooms.class);
		mMessages.put(Incoming.PopularRooms, PopularRooms.class);
		mMessages.put(Incoming.OwnRooms, OwnRooms.class);
		mMessages.put(Incoming.InitalizeRoom, InitalizeRoom.class);
		mMessages.put(Incoming.LoadHeightmap, LoadHeightmap.class);
		mMessages.put(Incoming.CanCreateRoom, CanCreateRoom.class);
		mMessages.put(Incoming.CreateNewRoom, CreateRoom.class);
		mMessages.put(Incoming.AddUserToRoom, RoomExternalData.class);
		mMessages.put(Incoming.AddUserToRoom2, RoomExternalData.class);
		mMessages.put(Incoming.SearchRoom, SearchRooms.class);
		mMessages.put(Incoming.LoadSearchRoom, SearchRooms.class);
		mMessages.put(Incoming.ExitRoom, LeaveRoom.class);
	}

	public void registerRooms()
	{
		mMessages.put(Incoming.KickUser, KickUser.class);
		mMessages.put(Incoming.Talk, Chat.class);
		mMessages.put(Incoming.Shout, Shout.class);
		mMessages.put(Incoming.Wisper, Wisper.class);
		mMessages.put(Incoming.ApplyDance, ApplyDance.class);
		mMessages.put(Incoming.ApplySign, ApplySign.class);
		mMessages.put(Incoming.ApplyAction, Wave.class);
		mMessages.put(Incoming.Walk, Walk.class);
		mMessages.put(Incoming.ChangeLooks, ChangeLooks.class);
		mMessages.put(Incoming.StartTyping, StartTyping.class);
		mMessages.put(Incoming.StopTyping, StopTyping.class);
		mMessages.put(Incoming.LoadRoomInfo, RoomSettings.class);
		mMessages.put(Incoming.LookTo, ClickHabbo.class);
		mMessages.put(Incoming.StartQuiz, StartQuiz.class);
		mMessages.put(Incoming.AnswerQuiz, AnswerQuiz.class);
		mMessages.put(Incoming.DeleteRoom, DeleteRoom.class);
		mMessages.put(Incoming.GiveRights, GiveRights.class);
		mMessages.put(Incoming.RemoveRights, RemoveRights.class);
		mMessages.put(Incoming.RemoveAllRights, RemoveAllRights.class);
	}
	
	public void registerTrade()
	{
		mMessages.put(Incoming.BeginTrade, BeginTrade.class);	
		mMessages.put(Incoming.AcceptTrade, AcceptTrade.class);
		mMessages.put(Incoming.ConfirmTrade, ConfirmTrade.class);
		mMessages.put(Incoming.UnacceptTrade, UnacceptTrade.class);
		mMessages.put(Incoming.CancelTrade, CancelTrade.class);
		mMessages.put(Incoming.SendOffer, OfferItem.class);
	}

	public void registerItems()
	{
		mMessages.put(Incoming.PurchaseItem, PurchaseItem.class);
		mMessages.put(Incoming.PurchaseGift, PurchaseGift.class);
		mMessages.put(Incoming.OpenInventory, OpenInventory.class);
		mMessages.put(Incoming.PlaceItem, PlaceItem.class);
		mMessages.put(Incoming.ChangeFloorItemPosition, MoveFloorItem.class);
		mMessages.put(Incoming.ChangeWallItemPosition, MoveWallItem.class);
		mMessages.put(Incoming.PickUpItem, PickUpItem.class);
		mMessages.put(Incoming.UpdatePapers, AddPapers.class);
		mMessages.put(Incoming.ChangeFloorItemState, ChangeFloorItemState.class);
		mMessages.put(Incoming.ChangeWallItemState, ChangeWallItemState.class);
		mMessages.put(Incoming.OpenDice, RollDice.class);
		mMessages.put(Incoming.RunDice, RollDice.class);
		mMessages.put(Incoming.ExchangeCoin, ExchangeCoin.class);
	}

	public void registerSnowstorm()
	{
	}

	public void registerModTool()
	{
		mMessages.put(Incoming.ModRoomInfo, ModGetRoom.class);
		mMessages.put(Incoming.ModRoomChat, ModGetRoomChatlog.class);
		mMessages.put(Incoming.ModUserInfo, ModUserInfo.class);
	}

	public void invokePacket(Session Session, Request Request, Class<? extends IMessage> event)
	{
		try
		{
			IMessage message = event.newInstance();

			for (Field field : message.getClass().getFields())
			{
				if (field.getType() == Boolean.class)
					field.setBoolean(message, Request.readBoolean());

				if (field.getType() == String.class)
					field.set(message, UserInputFilter.filterString(Request.readString(), true));

				if (field.getType() == Integer.class || field.getType() == int.class)
					field.setInt(message, Request.readInt());
			}


			message.request = Request;
			message.session = Session;

			message.handle();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void handleRequest(Session Session, Request Request) throws Exception
	{
		if (Sierra.getConfiguration().getProperty("log.session.receive").equals("1"))
		{
			if (Session.getHabbo().Username == null)
			{
				Log.writeLine("(Unknown) > RECEIEVED".concat(" ").concat(String.valueOf(Request.getMessageId()).concat(" / ").concat(Request.getMessageBody())));
			}
			else
			{
				Log.writeLine("(".concat(Session.getHabbo().Username).concat(") > RECEIEVED").concat(" ").concat(String.valueOf(Request.getMessageId()).concat(" / ").concat(Request.getMessageBody())));
			}
		}

		if (this.mMessages.containsKey(Request.getMessageId()))
		{
			Class<? extends IMessage> packet = mMessages.get(Request.getMessageId());

			IMessage message = packet.newInstance();

			for (Field field : message.getClass().getFields())
			{
				if (field.getType() == Boolean.class)
					field.setBoolean(message, Request.readBoolean());

				if (field.getType() == String.class)
					field.set(message, UserInputFilter.filterString(Request.readString(), true));

				if (field.getType() == Integer.class || field.getType() == int.class)
					field.setInt(message, Request.readInt());
			}

			message.session = Session;
			message.request = Request;

			message.handle();
		}
	}
}