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

package sierra.habbohotel.shop.cat;

import java.sql.ResultSet;
import java.util.*;

import sierra.Sierra;



public class ShopCategoryEngine
{
	private static List<ShopCategory> CatalogueIndexs = new ArrayList<ShopCategory>();
	
	public static List<ShopCategory> getCatalogueList(int Rank)
	{
		 List<ShopCategory> Indexs = new ArrayList<ShopCategory>();
		 
		 for (ShopCategory Index : CatalogueIndexs)
		 {
			 if (Index.getParentId() == -1 && (Index.getRank() == Rank || Rank > Index.getRank()))
			 {
				 Indexs.add(Index);
			 }
		 }
		 
		return Indexs;
	}
	
	public static List<ShopCategory> getSubCatalogueList(int Id, int Rank)
	{
		 List<ShopCategory> Indexs = new ArrayList<ShopCategory>();
		 
		 for (ShopCategory Index : CatalogueIndexs)
		 {
			 if (Index.getParentId() == Id && Index.getRank() <= Rank)
			 {
				 Indexs.add(Index);
			 }
		 }
		 
		return Indexs;
	}
	public static void load() throws Exception 
	{
		ResultSet Row = Sierra.getStorage().queryParams("SELECT * FROM catalog_pages ORDER BY order_num ASC;").executeQuery();

		while (Row.next())
		{
			CatalogueIndexs.add(new ShopCategory(Row.getInt("id"), Row.getInt("parent_id"), Row.getString("caption"), Row.getInt("icon_color"), Row.getInt("icon_image"), Row.getInt("min_rank"), Row.getInt("club_only")));
		}

		////Logger.writeLine("Cached [" + CatalogueIndexs.size() + "] catalogue categories");
	}
}
