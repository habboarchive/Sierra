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

package sierra.habbo.details;

public enum Rank 
{
	BANNED(0),
	NORMAL(1),
	VIP(2),
	SILVER_HOBBA(3),
	GOLD_HOBBA(4),
	SUPER_HOBBA(5),
	MODERATOR(6),
	ADMINISTRATOR(7);

	private int Rank;

	Rank(int rank)
	{
		this.Rank = rank;
	}

	static Rank valueOf(int i)
	{
		for (Rank rank : values())
		{
			if (rank.getRank() == i)
			{
				return rank;
			}
		}
		return null;
	}

	public int getRank() 
	{
		return this.Rank;
	}



}
