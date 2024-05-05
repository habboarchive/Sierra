package sierra.habbohotel;

import java.util.Properties;

import sierra.Log;
import sierra.habbohotel.allowances.AllowanceEngine;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.habbohotel.furniture.interactions.InteractorEngine;
import sierra.habbohotel.fuserights.FuserightEngine;
import sierra.habbohotel.guilds.GuildsPartsDataEngine;
import sierra.habbohotel.messenger.categories.MessengerCatEngine;
import sierra.habbohotel.navigator.cats.CategoryEngine;
import sierra.habbohotel.room.models.RoomModelEngine;
import sierra.habbohotel.shop.cat.ShopCategoryEngine;
import sierra.habbohotel.shop.items.ShopItemEngine;
import sierra.habbohotel.shop.pages.ShopPageEngine;
import sierra.habbohotel.wordfilter.WordfilterEngine;
import sierra.netty.connections.Connection;
import sierra.netty.mus.MusListener;
import sierra.plugin.PluginManager;
import sierra.storage.Storage;



public class HabboHotel {

	public static int ColourChatCrash = 24;
	
	protected static PluginManager pluginManager;
	protected static Storage mysql;
	protected static Connection connection;
	protected static Properties config;
	protected static MusListener musListener;
	
	protected static Boolean generateHotelData()
	{
		try
		{
			RoomModelEngine.load();
			ShopCategoryEngine.load();
			ShopPageEngine.load();
			FurnitureEngine.load();
			ShopItemEngine.load();
			MessengerCatEngine.load();
			CategoryEngine.load();
			FuserightEngine.load();
			WordfilterEngine.load();
			//ThreadingEngine.load();
			InteractorEngine.load();
			AllowanceEngine.load();
			GuildsPartsDataEngine.load();
			return true;
		}
		catch (Exception e)
		{
			Log.writeLine("Error, reason: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static Connection getSocketFactory()
	{
		return connection;
	}

	public static Storage getStorage()
	{
		return mysql;
	}

	public static Properties getConfiguration()
	{
		return config;
	}

	public static PluginManager getPluginManager() 
	{
		return pluginManager;
	}
}
