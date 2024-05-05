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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.pathfinder.Coord;
import sierra.habbohotel.pathfinder.Rotation;
import sierra.habbohotel.room.models.RoomModel;
import sierra.habbohotel.room.models.RoomModelEngine;
import sierra.habbohotel.trade.*;
import sierra.netty.readers.Response;
import sierra.utils.DateTime;



public class RoomUser
{
	public Session Session;
	public Trade Trade;
	public Room Room;
	public int UnitId;
	public int X;
	public int Y;
	public int GoalX;
	public int GoalY;
	public int BodyRotation;
	public double Height;
	public Boolean InRoom;
	public Boolean IsWalking;
	public Boolean NeedsUpdate;
	public Boolean IsSitting;
	public Boolean IsLayingDown;
	public Boolean IsTeleporting;
	public Boolean Forward;
	public Boolean Trading;
	public HashMap<String, String> Statuses;
	public LinkedList<Coord> Path;

	public RoomUser(Session Session)
	{
		this.Session = Session;
		this.Room = null;
		this.Statuses = new HashMap<String, String>();
		this.InRoom = false;
		this.NeedsUpdate = false;
		this.Forward = false;
		this.IsSitting = false;
		this.IsWalking = false;
		this.IsTeleporting = false;
		this.Trading = false;
		this.Height = 0;
		this.UnitId = 0;
	}

	public Boolean isOwner() {
		if (this.getRoom().getOwnerId() == this.getSession().getHabbo().Id)
			return true;
		else
			return false;
	}

	public void setBodyRotation() {
		this.BodyRotation = Rotation.Calculate(X, Y, GoalX, GoalY);
	}

	public int grabChatEmotion(String Message)
	{
		int Emotion = 0;

		if (Message.startsWith(":"))
		{
			if (Message.contains(":)") || Message.contains("=)") || Message.contains(":D") || Message.contains("=D"))
				Emotion = 1;

			if (Message.contains(":@") || Message.contains(">:(") || Message.contains(">:@"))
				Emotion = 2;

			if (Message.contains(":o") || Message.contains("D:"))
				Emotion = 3; 

			if (Message.contains(":(") || Message.contains(":'(") || Message.contains("=(") || Message.contains("='("))
				Emotion = 4;
		}

		return Emotion;
	}

	public void roomChat(String Message, Boolean Self, int Colour) throws SQLException
	{	
		Response response = this.getChatMessage(Outgoing.Talk, Message, grabChatEmotion(Message), Colour);

		if (Self)
			Session.sendResponse(response);
		else
			Session.sendRoom(response);
	}

	public void roomShout(String Message, Boolean Self, int Colour) throws SQLException
	{
		Response response = this.getChatMessage(Outgoing.Shout, Message, grabChatEmotion(Message), Colour);

		if (Self)
			Session.sendResponse(response);
		else
			Session.sendRoom(response);
	}
	
	private Response getChatMessage(int header, String Message, int Emotion, int Colour) throws SQLException
	{
		Session.getResponse().init(header);
		Session.getResponse().appendInt32(Session.getHabbo().Id);
		Session.getResponse().appendString(Message);
		Session.getResponse().appendInt32(Emotion);
		Session.getResponse().appendInt32(Colour);
		Session.getResponse().appendInt32(0);
		Session.getResponse().appendInt32(-1);

		if (Sierra.getConfiguration().getProperty("room.chatlogging").equalsIgnoreCase("true"))
		{
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO room_chatlogs (`room_id`, `full_date`, `timestamp`, `hour`, `minute`, `user_id`, `username`, `message`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
				{
					Statement.setInt(1, Session.getRoomUser().getRoom().getId());
					Statement.setString(2, DateTime.date().toString());
					Statement.setInt(3, Sierra.getUnixTime());
					Statement.setInt(4, DateTime.calendar().get(Calendar.HOUR_OF_DAY));
					Statement.setInt(5, DateTime.calendar().get(Calendar.MINUTE));
					Statement.setInt(6, Session.getHabbo().Id);
					Statement.setString(7, Session.getHabbo().Username);
					Statement.setString(8, Message);
					Statement.executeUpdate();
				}
		}

		return Session.getResponse();
	}

	public void teleportTo(int ToX, int ToY) {

		this.X = ToX;
		this.Y = ToY;

		Session.getResponse().init(Outgoing.RoomStatuses);
		Session.getResponse().appendInt32(1);
		Session.getResponse().appendInt32(Session.getHabbo().Id);
		Session.getResponse().appendInt32(ToX);
		Session.getResponse().appendInt32(ToY);
		Session.getResponse().appendString(Double.toString(Height));
		Session.getResponse().appendInt32(BodyRotation);
		Session.getResponse().appendInt32(BodyRotation);

		String sStatus = "/";

		for (Entry<String, String> set : Statuses.entrySet())
		{
			sStatus += set.getKey() + " " + set.getValue() + "/";
		}

		Session.getResponse().appendString(sStatus + "/");
		Session.sendRoom(Session.getResponse());
	}

	public void updateStatus() {	
		Session.getResponse().init(Outgoing.RoomStatuses);
		Session.getResponse().appendInt32(1);
		Session.getResponse().appendInt32(Session.getHabbo().Id);
		Session.getResponse().appendInt32(X);
		Session.getResponse().appendInt32(Y);
		Session.getResponse().appendString(Double.toString(Height));
		Session.getResponse().appendInt32(BodyRotation);
		Session.getResponse().appendInt32(BodyRotation);

		String sStatus = "/";

		for (Entry<String, String> set : Statuses.entrySet())
		{
			sStatus += set.getKey() + " " + set.getValue() + "/";
		}

		Session.getResponse().appendString(sStatus + "/");
		Session.sendRoom(Session.getResponse());
	}

	public void setIsSitting(boolean state) {
		this.IsSitting = state;

	}

	public void setIsLayingDown(boolean state) {
		this.IsLayingDown = state;

	}

	public RoomModel getModel() {
		return RoomModelEngine.getModelByName(this.Room.getModel());
	}
	public Session getSession() {
		return Session;
	}
	
	public Trade getTrade() {
		return Trade;
	}

	public boolean getIsTrading() {
		return Trading;
	}
	
	public Room getRoom() {
		return Room;
	}

	public int getUnitId() {
		return UnitId;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getGoalX() {
		return GoalX;
	}

	public int getGoalY() {
		return GoalY;
	}

	public int getBodyRotation() {
		return BodyRotation;
	}

	public double getHeight() {
		return Height;
	}

	public Boolean getInRoom() {
		return InRoom;
	}

	public Boolean getIsWalking() {
		return IsWalking;
	}

	public LinkedList<Coord> getPath() {
		return Path;
	}

	public void setPath(LinkedList<Coord> path) {
		Path = path;
	}

	public Boolean getNeedsUpdate() {
		return NeedsUpdate;
	}

	public void setNeedsUpdate(Boolean needsUpdate) {
		NeedsUpdate = needsUpdate;
	}

	public Boolean getIsSitting() {
		return IsSitting;
	}

	public Boolean getIsLayingDown() {
		return IsLayingDown;
	}

	public Boolean getIsTeleporting() {
		return IsTeleporting;
	}

	public Boolean getForward() {
		return Forward;
	}

	public HashMap<String, String> getStatuses() {
		return Statuses;
	}

	public void setSession(Session session) {
		Session = session;
	}
	
	public void setTrade(Trade trade) {
		Trade = trade;
	}

	public void setTrading(boolean trade) {
		Trading = trade;
	}
	
	public void setRoom(Room room) {
		Room = room;
	}

	public void setUnitId(int unitId) {
		UnitId = unitId;
	}

	public void setX(int x) {
		X = x;
	}

	public void setY(int y) {
		Y = y;
	}

	public void setGoalX(int goalX) {
		GoalX = goalX;
	}

	public void setGoalY(int goalY) {
		GoalY = goalY;
	}

	public void setRotation(int bodyRotation) {
		BodyRotation = bodyRotation;
	}

	public void setHeight(double height) {
		Height = height;
	}

	public void setInRoom(Boolean inRoom) {
		InRoom = inRoom;
	}

	public void setIsWalking(Boolean isWalking) {
		IsWalking = isWalking;
	}

	public void setIsTeleporting(Boolean isTeleporting) {
		IsTeleporting = isTeleporting;
	}

	public void setForward(Boolean forward) {
		Forward = forward;
	}

	public void setStatuses(HashMap<String, String> statuses) {
		Statuses = statuses;
	}

}
