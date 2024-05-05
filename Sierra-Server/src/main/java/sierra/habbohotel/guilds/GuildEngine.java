package sierra.habbohotel.guilds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sierra.Sierra;



public class GuildEngine {

	private static Map<Integer, Guild> guilds;
	
	static  {
		guilds = new HashMap<Integer, Guild>();
	}
	
	public static Map<Integer, Guild> getGuilds()
	{
		return guilds;
	}
	
	public static String getHTMLColor(int Color)
	{
		return Sierra.getStorage().readString("SELECT ExtraData1 FROM groups_elements WHERE Id = '" + Color + "' AND Type = 'Color3'");
	}

	public static Guild createGuild(String name, int ownerid, String ownername, String description, int roomid, String image, int customcolor1, int customcolor2, int guildbase, int guildbasecolor, List<Integer> guildstates, String htmlcolor1, String htmlcolor2, String datecreated, List<Integer> members, List<Integer> petitions, int type, int rightstype)
	{
        String states = "";
       
        for (int i : guildstates)
        {
            states += i + ";";
        }
        
        states = states.substring(0, states.length() - 1);
		
        int id = 0;
        
        try
		{
			PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO groups (name, owner_id, owner_name, description, room_id, image, custom_color1, custom_color2, guild_base, guild_base_color, guild_states, html_color1, html_color2, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			{
                Statement.setString(1, name);
                Statement.setInt(2, ownerid);
                Statement.setString(3, ownername);
                Statement.setString(4, description);
                Statement.setInt(5, roomid);
                Statement.setString(6, image);
                Statement.setInt(7, customcolor1);
                Statement.setInt(8, customcolor2);
                Statement.setInt(9, guildbase);
                Statement.setInt(10, guildbasecolor);
                Statement.setString(11, states);
                Statement.setString(12, htmlcolor1);
                Statement.setString(13, htmlcolor2);
                Statement.setString(14, datecreated);
				Statement.executeUpdate();
				
				ResultSet Keys = Statement.getGeneratedKeys();

				while (Keys.next())
				{
					id = Keys.getInt(1);
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        
        Guild guild = new Guild(id, name, ownerid, ownername, description, roomid, image, customcolor1, customcolor2, guildbase, guildbasecolor, guildstates, htmlcolor1, htmlcolor2, datecreated, members, petitions, type, rightstype, new HashMap<Integer, Integer>(), new ArrayList<String>());
       
		return guild;
	}
	
	public static Guild cacheGuild(Integer GuildId)
	{
        try
        {
        	ResultSet Row = Sierra.getStorage().readRow("SELECT * FROM `groups` WHERE id = '" + GuildId + "' LIMIT 1");
			
        	List<Integer> members = new ArrayList<Integer>();
            List<String> joindates = new ArrayList<String>();
        	List<Integer> petitions = new ArrayList<Integer>();
        	List<Integer> guildstates = new ArrayList<Integer>();
        	
        	for (String state : Row.getString("guild_states").split(";")) {
        		guildstates.add(Integer.parseInt(state));
            }
            
        	for (String state : Row.getString("petitions").split(";")) {
        		
        		if (state.length() == 0)
        			continue;
        		
        		petitions.add(Integer.parseInt(state));
            }
        	
        	HashMap<Integer, Integer> guildranks = new HashMap<Integer, Integer>();
        	
            ResultSet rowUser = Sierra.getStorage().queryParams("SELECT * FROM members_group_memberships WHERE group_id = '" + GuildId + "'").executeQuery();
            
            while (rowUser.next())
            {
                members.add(rowUser.getInt("user_id"));
                guildranks.put(rowUser.getInt("user_id"), rowUser.getInt("rank"));
                joindates.add(rowUser.getString("group_date"));
            }
            
            Guild guild = new Guild(GuildId, Row.getString("name"), Row.getInt("owner_id"), Row.getString("owner_name"), Row.getString("description"), Row.getInt("room_id"), Row.getString("image"), Row.getInt("custom_color1"), Row.getInt("custom_color2"), Row.getInt("guild_base"), Row.getInt("guild_base_color"), guildstates, Row.getString("html_color1"), Row.getString("html_color2"), Row.getString("date_created"), members, petitions, 0, Row.getInt("rights_type"), guildranks,joindates);
            		
            return guild;
        	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Full credits to Carlos D.
	 */
	public static String generateGuildImage(int GuildBase, int GuildBaseColor, List<Integer> GStates)
	{
		List<Integer> list = GStates;
		String str = "";
		int num = 0;
		String str2 = "b";
		if (String.valueOf(GuildBase).length() >= 2)
		{
			str2 = str2 + GuildBase;
		}
		else
		{
			str2 = str2 + "0" + GuildBase;
		}
		str = String.valueOf(GuildBaseColor);
		if (str.length() >= 2)
		{
			str2 = str2 + str;
		}
		else if (str.length() <= 1)
		{
			str2 = str2 + "0" + str;
		}
		int num2 = 0;
		if (list.get(9) != 0)
		{
			num2 = 4;
		}
		else if (list.get(6) != 0)
		{
			num2 = 3;
		}
		else if (list.get(3) != 0)
		{
			num2 = 2;
		}
		else if (list.get(0) != 0)
		{
			num2 = 1;
		}
		int num3 = 0;
		for (int i = 0; i < num2; i++)
		{
			str2 = str2 + "s";
			num = list.get(num3) - 20;
			if (String.valueOf(num).length() >= 2)
			{
				str2 = str2 + num;
			}
			else
			{
				str2 = str2 + "0" + num;
			}
			int num5 = list.get(1 + num3);
			str = String.valueOf(num5);
			if (str.length() >= 2)
			{
				str2 = str2 + str;
			}
			else if (str.length() <= 1)
			{
				str2 = str2 + "0" + str;
			}
			str2 = str2 + list.get(2 + num3).toString();
			switch (num3)
			{
			case 0:
				num3 = 3;
				break;

			case 3:
				num3 = 6;
				break;

			case 6:
				num3 = 9;
				break;
			}
		}
		return str2;
	}

	public static Guild getGroup(int groupId) {
		return guilds.containsKey(groupId) ? guilds.get(groupId) : cacheGuild(groupId);
	}

}

