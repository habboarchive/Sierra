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

package sierra.habbohotel.room.items.floor;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.messages.outgoing.item.UpdateFloorItemDataComposer;
import sierra.netty.readers.ISerialize;
import sierra.netty.readers.Response;

public class FloorItem implements ISerialize
{
	private int Id;
	private int BaseId;
	private int X;
	private int Y;
	private int Rotation;
	private Float Height;
	public String sHeight;
	public String ExtraData;
	
	public int getId() {
		return Id;
	}

	public int getBaseId() {
		return BaseId;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getRotation() {
		return Rotation;
	}

	public Float getHeight() {
		return Height;
	}

	public String getsHeight() {
		return sHeight;
	}

	public String getExtraData() {
		return ExtraData;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setBaseId(int baseId) {
		BaseId = baseId;
	}

	public void setX(int x) {
		X = x;
	}

	public void setY(int y) {
		Y = y;
	}

	public void setRotation(int rotation) {
		Rotation = rotation;
	}

	public void setHeight(Float height) {
		Height = height;
	}

	public void setsHeight(String sHeight) {
		this.sHeight = sHeight;
	}

	public void setExtraData(String extraData) {
		ExtraData = extraData;
	}
	
	public Furniture getItemInfo() {
		return FurnitureEngine.get(this.BaseId);
	}

	public FloorItem(int Id, int BaseId, int X, int Y, int Rotation, Float Height, String ExtraData)
	{
		this.Id = Id;
		this.BaseId = BaseId;
		this.X = X;
		this.Y = Y;
		this.Rotation = Rotation;
		this.Height = Height;
		this.sHeight = "" + Height;
		this.ExtraData = ExtraData;
	}
	
    /**
     * Thanks Shynoshy, ya TheJacob arselicking fuck!
     */
	public boolean changeExtraData()
	{
        if ((this.getItemInfo().getInteractionType().equals("default") || this.getItemInfo().getInteractionType().equals("gate")) && (this.getItemInfo().getInteractionModesCount() > 1)) {
            if (ExtraData.isEmpty() || ExtraData.equals(" ")) {
                ExtraData = "0";
            }

            Integer Temp = Integer.parseInt(ExtraData) + 1;

            if (Temp > this.getItemInfo().getInteractionModesCount()) {
                ExtraData = "0";
            } else {
                ExtraData = "" + Temp;
            }

            return true;
        } else {
            return false;
        }
	}

	public void saveExtraData()
	{
		try 
		{
			Sierra.getStorage().queryParams("UPDATE room_items SET extra = '" + ExtraData + "' WHERE id = '" + Id + "';").execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void serialize(Response Message)
	{
		Message.appendInt32(Id);
		Message.appendInt32(getItemInfo().getSpriteId());
		Message.appendInt32(X);
		Message.appendInt32(Y);
		Message.appendInt32(Rotation);
		Message.appendString(Float.toString(Height));
		Message.appendInt32(0);
		Message.appendInt32(0);
		Message.appendString(ExtraData);
		Message.appendInt32(-1);
		Message.appendInt32(getItemInfo().getInteractionType().equals("default") ? 1 : 0);
	}

	public void sendUpdate(Session Session) {
		
		Session.sendRoom(new UpdateFloorItemDataComposer(Id, ExtraData));
	}
}
