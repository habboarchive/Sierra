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

package sierra.habbohotel.furniture;

import sierra.habbohotel.abstracts.IFurniture;
import sierra.habbohotel.furniture.interactions.VendingMachineAction;
import sierra.habbohotel.shop.pages.ShopPage;
import sierra.habbohotel.shop.pages.ShopPageEngine;

public class Furniture extends IFurniture
{    
	public Furniture(int Id, String PublicName, String ItemName, String Type, int Width, int Length, Float StackHeight, int CanStack, int CanSit,  int IsWalkable, int SpriteId, int AllowRecycle, int AllowTrade, int AllowMarketplaceSell, int AllowGift, int AllowInventoryStack, String InteractionType, int InteractionModesCount, String VendingIds, int IsArrow)
	{
		this.Id = Id;
		this.PublicName = PublicName;
		this.ItemName = ItemName;
		this.Type = Type;
		this.Width = Width;
		this.Length = Length;
		this.StackHeight = StackHeight;
		this.CanStack = CanStack == 1;
		this.CanSit = CanSit == 1;
		this.IsWalkable = IsWalkable == 1;
		this.SpriteId = SpriteId;
		this.AllowRecycle = AllowRecycle == 1;
		this.AllowTrade = AllowTrade == 1;
		this.AllowMarketplaceSell = AllowMarketplaceSell == 1;
		this.AllowGift = AllowGift == 1;
		this.AllowInventoryStack = AllowInventoryStack == 1;
		this.IsArrow = IsArrow == 1;
		this.InteractionType = InteractionType;
		this.InteractionModesCount = InteractionModesCount;
		
		this.vendingIds = VendingMachineAction.asList(VendingMachineAction.convertStringArraytoIntArray(VendingIds.split(",")));
	}
	
	public ShopPage getShopPage()
	{
		return ShopPageEngine.getPageByItemBase(this.Id);
	}
}
