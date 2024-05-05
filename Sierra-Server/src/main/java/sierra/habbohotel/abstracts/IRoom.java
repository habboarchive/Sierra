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

package sierra.habbohotel.abstracts;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbo.Session;
import sierra.habbohotel.guilds.Guild;
import sierra.habbohotel.guilds.GuildEngine;
import sierra.habbohotel.pets.Pet;
import sierra.habbohotel.room.items.floor.FloorItem;
import sierra.habbohotel.room.items.wall.WallItem;



public abstract class IRoom
{
	public int Id;
	protected int OwnerId;
	protected int GroupId;
	protected int State;
	protected int Score = 0;
	protected int Category = 0;
	protected int MaximumInRoom;
	protected String Floor;
	protected String Description;
	protected String Model;
	protected String OwnerName;
	protected String Name;
	protected String Wall;
	protected String Landscape;
	protected String TagFormat;
	protected Boolean AllowPets;
	protected Boolean AllowPetsEat;
	protected Boolean AllowWalkthrough;
	protected Boolean HideWall;
	protected Integer WallThickness;
	protected Integer FloorThickness;
	protected List<String> Tags;
	protected ConcurrentLinkedQueue<Session> Users;
	protected ConcurrentLinkedQueue<Pet> Pets;
	protected ConcurrentLinkedQueue<Integer> Rights;
	protected ConcurrentLinkedQueue<WallItem> WallItems;
	protected ConcurrentLinkedQueue<FloorItem> FloorItems;
	
	public int getId() {
		return Id;
	}

	public int getOwnerId() {
		return OwnerId;
	}

	public int getGroupId() {
		return GroupId;
	}

	public void setGroupId(int groupId) {
		GroupId = groupId;
	}
	
	public boolean hasGroup() {
		return GroupId != 0;
	}
	
	public Guild getGroup() {
		return GuildEngine.getGroup(GroupId);
	}

	public int getState() {
		return State;
	}

	public int getScore() {
		return Score;
	}

	public int getCategory() {
		return Category;
	}

	public String getFloor() {
		return Floor;
	}

	public String getDescription() {
		return Description;
	}

	public String getModel() {
		return Model;
	}

	public String getOwnerName() {
		return OwnerName;
	}

	public String getName() {
		return Name;
	}
	
	public String getWall() {
		return Wall;
	}

	public String getLandscape() {
		return Landscape;
	}

	public List<String> getTags() {
		return Tags;
	}

	public int getMaximumInRoom() {
		return MaximumInRoom;
	}

	public ConcurrentLinkedQueue<Session> getUsers() {
		return Users;
	}

	public ConcurrentLinkedQueue<Pet> getPets() {
		return Pets;
	}

	public ConcurrentLinkedQueue<Integer> getRights() {
		return Rights;
	}

	public ConcurrentLinkedQueue<WallItem> getWallItems() {
		return WallItems;
	}

	public ConcurrentLinkedQueue<FloorItem> getFloorItems() {
		return FloorItems;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setOwnerId(int ownerId) {
		OwnerId = ownerId;
	}

	public void setState(int state) {
		State = state;
	}

	public void setScore(int score) {
		Score = score;
	}

	public void setCategory(int category) {
		Category = category;
	}

	public void setFloor(String floor) {
		Floor = floor;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setModel(String model) {
		Model = model;
	}

	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setWall(String wall) {
		Wall = wall;
	}

	public void setLandscape(String landscape) {
		Landscape = landscape;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}

	public void setUsers(ConcurrentLinkedQueue<Session> users) {
		Users = users;
	}

	public void setPets(ConcurrentLinkedQueue<Pet> pets) {
		Pets = pets;
	}

	public void setRights(ConcurrentLinkedQueue<Integer> rights) {
		Rights = rights;
	}

	public void setWallItems(ConcurrentLinkedQueue<WallItem> wallItems) {
		WallItems = wallItems;
	}

	public void setFloorItems(ConcurrentLinkedQueue<FloorItem> floorItems) {
		FloorItems = floorItems;
	}
	
	public Boolean getAllowPets() {
		return AllowPets;
	}

	public Boolean getAllowPetsEat() {
		return AllowPetsEat;
	}

	public boolean getAllowWalkthrough() {
		return AllowWalkthrough;
	}

	public void setAllowWalkthrough(boolean allowWalkthrough) {
		AllowWalkthrough = allowWalkthrough;
	}

	public Boolean getHideWall() {
		return HideWall;
	}

	public Integer getWallThickness() {
		return WallThickness;
	}

	public Integer getFloorThickness() {
		return FloorThickness;
	}

	public void setAllowPets(Boolean allowPets) {
		AllowPets = allowPets;
	}

	public void setAllowPetsEat(Boolean allowPetsEat) {
		AllowPetsEat = allowPetsEat;
	}

	public void setHideWall(Boolean hideWall) {
		HideWall = hideWall;
	}

	public void setWallThickness(Integer wallThickness) {
		WallThickness = wallThickness;
	}

	public void setFloorThickness(Integer floorThickness) {
		FloorThickness = floorThickness;
	}
}
