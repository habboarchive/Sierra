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

package sierra.habbohotel.room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.abstracts.IRoom;
import sierra.habbohotel.furniture.FurnitureWalkingInteractions;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.pathfinder.AffectedTile;
import sierra.habbohotel.pathfinder.Coord;
import sierra.habbohotel.pathfinder.Rotation;
import sierra.habbohotel.pets.Pet;
import sierra.habbohotel.room.items.floor.*;
import sierra.habbohotel.room.items.wall.*;
import sierra.habbohotel.room.models.RoomModelEngine;
import sierra.messages.outgoing.room.DisposeFigureComposer;
import sierra.messages.outgoing.room.HotelViewComposer;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;



public class Room extends IRoom implements ISerialize, Runnable
{
	public boolean RoomActive;

	public Room(int Id)
	{
		this.Id = Id;
		this.Tags = new ArrayList<String>();
		this.Users = new ConcurrentLinkedQueue<Session>();
		this.Pets = new ConcurrentLinkedQueue<Pet>();
		this.Rights = new ConcurrentLinkedQueue<Integer>();
		this.WallItems = WallItemEngine.wallItems(Id);
		this.FloorItems = FloorItemEngine.floorItems(Id);
		this.RoomActive = true;
	}

	public Room fillData(ResultSet Row, int OwnerId, String OwnerName) throws SQLException
	{
		this.OwnerId = OwnerId;
		this.OwnerName = OwnerName;
		this.GroupId = Row.getInt("group_id");
		this.Name = Row.getString("name");
		this.Description = Row.getString("description");
		this.Model = Row.getString("model");
		this.Wall = Row.getString("wallpaper");
		this.Floor = Row.getString("floor");
		this.Landscape = Row.getString("outside");
		this.MaximumInRoom = RoomModelEngine.getModelByName(this.Model).getMapSizeX() >= 20 || RoomModelEngine.getModelByName(this.Model).getMapSizeY() >= 20 ? 50 : 25;
		this.AllowPets = Row.getBoolean("allow_pets");
		this.AllowPetsEat = Row.getBoolean("allow_pets_eat");
		this.AllowWalkthrough = Row.getBoolean("allow_walkthrough");
		this.HideWall = Row.getBoolean("hidewall");
		this.WallThickness = Row.getInt("wall_thickness");
		this.FloorThickness = Row.getInt("floor_thickness");
		this.TagFormat = Row.getString("tags");

		this.parseTags();
		this.parseUserRights();

		Thread thread = new Thread(this);
		thread.start();

		return this;
	}

	public void parseTags()
	{
		for (String tag : TagFormat.split(";"))
		{
			this.Tags.add(tag);
		}
	}

	public void parseUserRights() 
	{
		try
		{
			ResultSet RightsRow = Sierra.getStorage().queryParams("SELECT * FROM room_rights WHERE roomid = '" + this.Id + "'").executeQuery();

			while (RightsRow.next())
			{
				this.Rights.add(RightsRow.getInt("userid"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public WallItem addWallItem(int _Id, int BaseId, String Position, String ExtraData)
	{
		WallItem item = new WallItem(_Id, BaseId, Position, ExtraData);

		WallItems.add(item);

		return item;
	}

	public FloorItem addFloorItem(int _Id, int BaseId, int X, int Y, int Rot, Float Height, String ExtraData)
	{
		FloorItem item = new FloorItem(_Id, BaseId, X, Y, Rot, Height, ExtraData);

		FloorItems.add(item);

		return item;
	}

	public boolean isRoomFull()
	{
		return Users.size() == MaximumInRoom || Users.size() > MaximumInRoom;
	}

	public Boolean hasRights(Boolean OnlyOwner, Session user)
	{ 
		if (OnlyOwner)
		{
			return OwnerId == user.getHabbo().Id;
		}
		else
		{
			if (Rights.contains(user.getHabbo().Id) || OwnerId == user.getHabbo().Id || user.getHabbo().hasFuse("fuse_any_room_rights"))
				return true;
			else
				return false;
		}
	}

	public Session getUserByVirtualId(int Id)
	{
		for (Session session : Users)
		{
			// User id for now
			if (session.getHabbo().Id == Id)
			{
				return session;
			}
		}

		return null;
	}

	public Boolean getUserExistsAt(int Id, int X, int Y)
	{
		for (Session User : Users)
		{
			if (User.getHabbo().Id != Id)
			{
				if (User.getRoomUser().getX() == X && User.getRoomUser().getY() == Y)
					return true;

				if (User.getRoomUser().getGoalX() == X && User.getRoomUser().getGoalY() == Y)
					return true;
			}
		}
		return false;
	}

	public Session getUserAt(int X, int Y)
	{
		for (Session User : Users)
		{
			if (User.getRoomUser().getX() == X && User.getRoomUser().getY() == Y)
				return User;

			if (User.getRoomUser().getGoalX() == X && User.getRoomUser().getGoalY() == Y)
				return User;
		}
		return null;
	}

	public WallItem getWallItem(int Id)
	{
		for (WallItem Item : WallItems)
		{
			if (Item.Id == Id)
				return Item;
		}
		return null;
	}

	public FloorItem getFloorItem(int Id)
	{
		for (FloorItem Item : FloorItems)
		{
			if (Item.getId() == Id)
				return Item;
		}
		return null;
	}

	public List<FloorItem> getItemsAt(int X, int Y)
	{
		List<FloorItem> Result = new ArrayList<FloorItem>();

		for (FloorItem Item : FloorItems)
		{
			if (Item.getX() == X && Item.getY() == Y) 
			{
				Result.add(Item);
			}
			else
			{
				List<AffectedTile> AffectedTiles = AffectedTile.getAffectedTilesAt(Item.getItemInfo().getLength(), Item.getItemInfo().getWidth(), Item.getX(), Item.getY(), Item.getRotation());

				for (AffectedTile Tile : AffectedTiles)
				{
					if (X == Tile.X && Y == Tile.Y)
					{
						if (Result.contains(Item) == false)
						{
							Result.add(Item);
						}
					}
				}
			}
		}
		return Result;
	}

	public Map<Boolean, FloorItem> getWalkableItemsAt(int X, int Y)
	{
		Map<Boolean, FloorItem> Result = new HashMap<Boolean, FloorItem>();

		for (FloorItem Item : FloorItems)
		{
			if (Item.getX() == X && Item.getY() == Y) 
			{
				Result.put(false, Item);
			}
			else
			{
				List<AffectedTile> AffectedTiles = AffectedTile.getAffectedTilesAt(Item.getItemInfo().getLength(), Item.getItemInfo().getWidth(), Item.getX(), Item.getY(), Item.getRotation());

				for (AffectedTile Tile : AffectedTiles)
				{
					if (X == Tile.X && Y == Tile.Y)
					{
						if (Result.containsValue(Item) == false)
						{
							Result.put(false, Item);
						}
					}

				}
			}
		}
		return Result;
	}

	public FloorItem getItemAt(int X, int Y)
	{
		for (FloorItem Item : FloorItems)
		{
			for (AffectedTile Tile : AffectedTile.getAffectedTilesAt(Item.getItemInfo().getLength(), Item.getItemInfo().getWidth(), Item.getX(), Item.getY(), Item.getRotation()))
			{
				if (Item.getX() == X && Item.getY() == Y || Tile.X == X && Tile.Y == Y)
				{
					return Item;
				}
			}
		}
		return null;
	}

	public void leaveRoom(Boolean HotelView, Boolean Dispose, Session session)
	{        
		session.sendRoom(new DisposeFigureComposer(session.getHabbo().Id));
		session.getRoomUser().setInRoom(false);

		if (session.getRoomUser().getIsTrading())
		{
			session.getRoomUser().getTrade().closeTrade(session.getHabbo().Id);
		}

		if (session.getRoomUser().getIsWalking())
		{
			if (session.getRoomUser().getStatuses().containsKey("mv"))
			{
				session.getRoomUser().getStatuses().remove("mv");
			}

			session.getRoomUser().setNeedsUpdate(true);
			session.getRoomUser().setIsWalking(false);
		}

		if (HotelView) {
			session.sendResponse(new HotelViewComposer());	
		}

		Users.remove(session);
		session.getRoomUser().getStatuses().clear();
		session.getRoomUser().setRoom(null);
		session.getRoomUser().setIsSitting(false);

		if (Dispose) 
		{
			if (!OwnerName.equals(session.getHabbo().Username))
				RoomEngine.removeById(Id);
		}
	}

	public void sendRoom(Response Response)
	{
		for (Session client : Users)
		{
			client.sendResponse(Response);
		}
	}

	public void SerializeInfo(Response Message)
	{
		Message.appendBoolean(true);
		Message.appendInt32(Id);
		Message.appendString(Name);
		Message.appendBoolean(true);
		Message.appendInt32(OwnerId);
		Message.appendString(OwnerName);
		Message.appendInt32(State); // room state
		Message.appendInt32(this.Users.size());
		Message.appendInt32(this.getMaximumInRoom());
		Message.appendString(Description);
		Message.appendInt32(0); // dunno!
		Message.appendInt32(0); // can trade?
		Message.appendInt32(Score);
		Message.appendInt32(0);
		Message.appendInt32(Category);

		if (this.hasGroup())
		{
			Message.appendInt32(this.getGroupId());
			Message.appendString(this.getGroup().getName());
			Message.appendString(this.getGroup().getImage());
		}
		else
		{
			Message.appendInt32(0);
			Message.appendInt32(0);
		}
		Message.appendString("");

		Message.appendInt32(Tags.size());

		for (String tag : Tags)
		{
			Message.appendString(tag);
		}

		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendBoolean(true);
		Message.appendBoolean(true);
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendBoolean(false);
		Message.appendBoolean(false);
		Message.appendBoolean(false);
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendBoolean(false);
		Message.appendBoolean(true);

	}

	@Override
	public void serialize(Response Message)
	{
		Message.appendInt32(Id);
		Message.appendString(Name);
		Message.appendBoolean(true);
		Message.appendInt32(OwnerId);
		Message.appendString(OwnerName);
		Message.appendInt32(State); // room state
		Message.appendInt32(this.Users.size());
		Message.appendInt32(this.getMaximumInRoom());
		Message.appendString(Description);
		Message.appendInt32((Category == 3) ? 2 : 0); // dunno!
		Message.appendInt32(0); // can trade?
		Message.appendInt32(Score);
		Message.appendInt32(0);
		Message.appendInt32(Category);
		if (this.hasGroup())
		{
			Message.appendInt32(this.getGroupId());
			Message.appendString(this.getGroup().getName());
			Message.appendString(this.getGroup().getImage());
		}
		else
		{
			Message.appendInt32(0);
			Message.appendInt32(0);
		}
		Message.appendString("");

		Message.appendInt32(Tags.size());

		for (String tag : Tags)
		{
			Message.appendString(tag);
		}

		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendBoolean(true);
		Message.appendBoolean(true);
		Message.appendInt32(0);
		Message.appendInt32(0);
	}

	public void sendUserFigures(Session session)
	{
		session.getResponse().init(Outgoing.RoomUsers);
		session.getResponse().appendInt32(Users.size());

		for (Session user : Users)
		{
			session.getResponse().appendInt32(user.getHabbo().Id);
			session.getResponse().appendString(user.getHabbo().Username);
			session.getResponse().appendString(user.getHabbo().Motto);
			session.getResponse().appendString(user.getHabbo().Figure);
			session.getResponse().appendInt32(user.getHabbo().Id); // ROOM ENTITY ID: Haven't coded pets, nor bots, so it's just the user id
			session.getResponse().appendInt32(user.getRoomUser().getX());
			session.getResponse().appendInt32(user.getRoomUser().getY());
			session.getResponse().appendString(Double.toString(user.getRoomUser().getHeight()));
			session.getResponse().appendInt32(user.getRoomUser().getBodyRotation());
			session.getResponse().appendInt32(1);
			session.getResponse().appendString("m");
			session.getResponse().appendInt32(-1);
			session.getResponse().appendInt32(-1);
			session.getResponse().appendInt32(0);
			session.getResponse().appendInt32(1337); //TODO: User achievement
		}
		this.sendRoom(session.getResponse());
	}

	public void sendUserPositions(Session session)
	{
		session.getResponse().init(Outgoing.RoomStatuses);
		session.getResponse().appendInt32(Users.size());
		for (Session user : Users)
		{
			session.getResponse().appendInt32(user.getHabbo().Id);
			session.getResponse().appendInt32(user.getRoomUser().getX());
			session.getResponse().appendInt32(user.getRoomUser().getY());
			session.getResponse().appendString(Double.toString(user.getRoomUser().getHeight()));
			session.getResponse().appendInt32(user.getRoomUser().getBodyRotation());
			session.getResponse().appendInt32(user.getRoomUser().getBodyRotation());

			String Status = "/";

			for (Entry<String, String> set : user.getRoomUser().getStatuses().entrySet())
			{
				Status += set.getKey() + " " + set.getValue() + "/";
			}

			session.getResponse().appendString(Status + "/");
		}
		this.sendRoom(session.getResponse());
	}

	/*
	 * Thank you ItachiKM (Julian) and John (Olympian) for showing/motivating me
	 * how to properly implement a pathfinder!
	 */

	@Override
	public void run() {

		while (RoomActive)
		{
			if (!RoomActive)
			{
				break;
			}

			List<Session> usersToUpdate = new ArrayList<Session>();

			synchronized (this.Users)
			{
				for (Session user : this.Users)
				{			
					RoomUser roomUser = user.getRoomUser();

					if (user == null || roomUser == null)
					{
						this.Users.remove(user);
						continue;
					}

					if (roomUser.getIsWalking() && roomUser.getPath().size() > 0)
					{
						Coord next = roomUser.getPath().poll();

						if (roomUser.getStatuses().containsKey("mv"))
						{
							roomUser.getStatuses().remove("mv");
						}

						if (roomUser.getStatuses().containsKey("sit"))
						{
							roomUser.getStatuses().remove("sit");
						}

						if (roomUser.getStatuses().containsKey("lay"))
						{
							roomUser.getStatuses().remove("lay");
						}

						int Rot = Rotation.Calculate(roomUser.getX(), roomUser.getY(), next.X, next.Y);
						roomUser.setRotation(Rot);

						roomUser.getStatuses().put("mv", String.valueOf(next.X).concat(",").concat(String.valueOf(next.Y)).concat(",").concat(String.valueOf(roomUser.getModel().getSquareHeight()[next.X][next.Y])));
						roomUser.updateStatus();

						roomUser.setX(next.X);
						roomUser.setY(next.Y);

						roomUser.setHeight(roomUser.getModel().getSquareHeight()[next.X][next.Y]);

						for (FloorItem item : roomUser.getRoom().getItemsAt(roomUser.getX(), roomUser.getY()))
						{
							if (item.getItemInfo().getIsWalkable())
							{
								roomUser.setHeight(roomUser.getHeight() + item.getItemInfo().getStackHeight());
							}
						}

					} 
					else if (roomUser.getIsWalking())
					{
						if (roomUser.getStatuses().containsKey("mv"))
						{
							roomUser.getStatuses().remove("mv");
						}

						roomUser.setNeedsUpdate(true);
						roomUser.setIsWalking(false);
					}

					if (roomUser.getNeedsUpdate())
					{
						if (roomUser.getStatuses().containsKey("mv") && !roomUser.getIsWalking())
						{
							roomUser.getStatuses().remove("mv");
						}

						for (FloorItem Item : roomUser.getRoom().getItemsAt(roomUser.getX(), roomUser.getY()))
						{
							if (Item.getItemInfo().getCanSit())
							{
								FurnitureWalkingInteractions.walkedOnSeat(Item, roomUser);
								break;
							}
							if (Item.getItemInfo().getIsType("bed"))
							{
								FurnitureWalkingInteractions.walkedOnBed(Item, roomUser);
								break;
							}
						}

						usersToUpdate.add(user);
						roomUser.setNeedsUpdate(false);
						roomUser.setIsWalking(false);
					}

				}
			}

			Response update = new Response();

			update.init(Outgoing.RoomStatuses);
			update.appendInt32(usersToUpdate.size());

			for (Session user : usersToUpdate)
			{
				RoomUser roomUser = user.getRoomUser();

				update.appendInt32(user.getHabbo().Id);
				update.appendInt32(user.getRoomUser().getX());
				update.appendInt32(user.getRoomUser().getY());
				update.appendString(Double.toString(user.getRoomUser().getHeight()));
				update.appendInt32(user.getRoomUser().getBodyRotation());
				update.appendInt32(user.getRoomUser().getBodyRotation());

				String Status = "/";

				for (Entry<String, String> set : user.getRoomUser().getStatuses().entrySet())
				{
					Status += set.getKey() + " " + set.getValue() + "/";
				}

				update.appendString(Status + "/");

				if (roomUser.getInRoom() && roomUser.getX() == roomUser.getModel().getDoorX() && roomUser.getY() == roomUser.getModel().getDoorY()) 
				{
					roomUser.getRoom().leaveRoom(true, false, user);
				}
			}

			if (usersToUpdate.size() != 0)
				this.sendRoom(update);

			usersToUpdate.clear();
			usersToUpdate = null;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
