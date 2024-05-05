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

public class IFurniture
{
	protected int Id;
	protected String PublicName;
	protected String ItemName;
	protected String Type;
	protected int Width;
	protected int Length;
	protected Float StackHeight;
	protected Boolean CanStack;
	protected Boolean CanSit;
	protected Boolean IsWalkable;
	protected int SpriteId;
	protected Boolean AllowRecycle;
	protected Boolean AllowTrade;
	protected Boolean AllowMarketplaceSell;
    protected Boolean AllowGift;
    protected Boolean AllowInventoryStack;
    protected Boolean IsArrow;
    protected String InteractionType;
    protected int InteractionModesCount;
    protected List<Integer> vendingIds;
	
	public int getId() {
		return Id;
	}

	public String getPublicName() {
		return PublicName;
	}

	public String getItemName() {
		return ItemName;
	}

	public String getType() {
		return Type;
	}

	public int getWidth() {
		return Width;
	}

	public int getLength() {
		return Length;
	}

	public Float getStackHeight() {
		return StackHeight;
	}

	public Boolean getCanStack() {
		return CanStack;
	}

	public Boolean getCanSit() {
		return CanSit;
	}

	public Boolean getIsType(String type) {
		return InteractionType.equals(type);
	}
	
	public Boolean getIsWalkable() {
		return IsWalkable;
	}

	public int getSpriteId() {
		return SpriteId;
	}

	public Boolean getAllowRecycle() {
		return AllowRecycle;
	}

	public Boolean getAllowTrade() {
		return AllowTrade;
	}

	public Boolean getAllowMarketplaceSell() {
		return AllowMarketplaceSell;
	}

	public Boolean getAllowGift() {
		return AllowGift;
	}

	public Boolean getAllowInventoryStack() {
		return AllowInventoryStack;
	}

	public String getInteractionType() {
		return InteractionType;
	}

	public int getInteractionModesCount() {
		return InteractionModesCount;
	}

	public List<Integer> getVendingIds() {
		return vendingIds;
	}

	public Boolean getIsArrow() {
		return IsArrow;
	}
}
