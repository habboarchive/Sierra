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

package sierra.habbohotel.shop.pages;

import sierra.habbohotel.abstracts.IShopPage;

public class ShopPage extends IShopPage	
{
	public ShopPage(int Id, String Caption, String Layout, String Headline, String Teaser, String Special, String Text1, String Text2, String Details, String Teaser2)
	{
		this.Id = Id;
		this.Caption = Caption;
		this.Layout = Layout;
		this.Headline = Headline;
		this.Teaser = Teaser;
		this.Special = Special;
		this.Text1 = Text1;
		this.Text2 = Text2;
		this.Details = Details;
		this.Teaser2 = Teaser2;
	}
}