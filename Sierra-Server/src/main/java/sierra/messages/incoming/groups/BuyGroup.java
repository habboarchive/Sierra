package sierra.messages.incoming.groups;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sierra.Log;
import sierra.Sierra;
import sierra.habbohotel.guilds.Guild;
import sierra.habbohotel.guilds.GuildEngine;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.messages.IMessage;
import sierra.messages.outgoing.groups.GuildHTMLColoursComposer;
import sierra.messages.outgoing.groups.PurchasedGuildComposer;
import sierra.utils.DateTime;



public class BuyGroup extends IMessage {

    private List<Integer> GuildStates = new ArrayList<Integer>();
    
    public String name;
    public String description;
    public int roomid;
    public int colour;
    public int colour2;
    public int Junk;
    public int guildBase;
    public int guildBaseColour;
    public int amount;
	
	@Override
	public void handle() throws Exception {
		
		for (int i = 0; i < amount * 3; i++)
        {
            int item = this.request.readInt();
            
            GuildStates.add(item);
        }

		Room room = RoomEngine.getRoom(this.roomid);
		
		if (room.hasGroup())
		{
			session.sendNotify("This room already has a group!");
			return;
		}
		
		String image = GuildEngine.generateGuildImage(guildBase, guildBaseColour, GuildStates);
        String htmlColour = GuildEngine.getHTMLColor(colour);
        String htmlColour2 = GuildEngine.getHTMLColor(amount);
        String datecreated = DateTime.now();
        
        int guildOwnerId = session.getHabbo().Id;
        String guildOwnerName = session.getHabbo().Username;
        
        List<Integer> members = new ArrayList<Integer>();
        members.add(guildOwnerId);
        
        Guild guild = GuildEngine.createGuild(name, guildOwnerId, guildOwnerName, description, roomid, image, colour, colour2, guildBase, guildBaseColour, GuildStates, htmlColour, htmlColour2, datecreated, members, new ArrayList<Integer>(), 0, 0);
        
		PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_group_memberships (username, user_id, group_id, group_date, rank) VALUES (?, ?, ?, ?, ?)");
		{
			Statement.setString(1, guildOwnerName);
			Statement.setInt(2, guildOwnerId);
			Statement.setInt(3, guild.getId());
			Statement.setString(4, DateTime.calendar().get(Calendar.DAY_OF_MONTH) + " - " + DateTime.calendar().get(Calendar.MONTH) + " - " + DateTime.calendar().get(Calendar.YEAR));
			Statement.setInt(5, 0);
			Statement.executeUpdate();
		}
        
        room.setGroupId(guild.getId());
        
        Sierra.getStorage().executeQuery("UPDATE rooms SET group_id = '" + guild.getId() + "' WHERE id = '" + roomid + "'");
        
        session.getHabbo().GroupMemberships.add(guild.getId());
        
        session.sendResponse(new PurchasedGuildComposer());
        session.sendResponse(new GuildHTMLColoursComposer(session.getHabbo().GroupMemberships));
        
	}
}
