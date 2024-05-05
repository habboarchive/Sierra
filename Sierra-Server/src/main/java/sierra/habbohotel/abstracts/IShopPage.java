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

public class IShopPage
{
	protected int Id;
	protected String Caption;
	protected String Layout;
	protected String Headline;
	protected String Teaser;
	protected String Special;
	protected String Text1;
	protected String Text2;
	protected String Details;
	protected String Teaser2;
	
	public int getId() {
		return Id;
	}

	public String getCaption() {
		return Caption;
	}

	public String getLayout() {
		return Layout;
	}

	public String getHeadline() {
		return Headline;
	}

	public String getTeaser() {
		return Teaser;
	}

	public String getSpecial() {
		return Special;
	}

	public String getText1() {
		return Text1;
	}

	public String getText2() {
		return Text2;
	}

	public String getDetails() {
		return Details;
	}

	public String getTeaser2() {
		return Teaser2;
	}
}
