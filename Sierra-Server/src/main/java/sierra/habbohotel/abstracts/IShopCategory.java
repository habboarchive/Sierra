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

public class IShopCategory
{
	protected int Id;
	protected int ParentId;
	protected String Label;
	protected int Color;
	protected int Icon;
	protected int Rank;
	protected int Club;
	
	public int getId() {
		return Id;
	}

	public int getParentId() {
		return ParentId;
	}

	public String getLabel() {
		return Label;
	}

	public int getColor() {
		return Color;
	}

	public int getIcon() {
		return Icon;
	}

	public int getRank() {
		return Rank;
	}

	public int getClub() {
		return Club;
	}
}
