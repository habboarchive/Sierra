package sierra.messages.incoming.groups;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Log;
import sierra.habbohotel.guilds.Guild;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class ManageGroupComposer extends ICompose {

	public Guild Group;
	public ConcurrentLinkedQueue<Room> Rooms;
	
	public ManageGroupComposer(Guild group, ConcurrentLinkedQueue<Room> rooms) {
		super();
		this.Group = group;
		this.Rooms = rooms;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.GroupModifyInfo);
		
        response.appendInt32(this.Rooms.size());
        for (Room room : this.Rooms)
        {
            response.appendInt32(room.getId());
            response.appendString(room.getName());
            response.appendBoolean(false);
        }
		
		response.appendBoolean(true);
        response.appendInt32(this.Group.getId());
        response.appendString(this.Group.getName());
        response.appendString(this.Group.getDescription());
        response.appendInt32(this.Group.getRoomId());
        response.appendInt32(this.Group.getCustomColor1());
        response.appendInt32(this.Group.getCustomColor2());
        response.appendInt32(this.Group.getType());
        response.appendInt32(this.Group.getRightsType());
        response.appendBoolean(false);
        response.appendString("");
        response.appendInt32(5);
        response.appendInt32(this.Group.getGuildBase());
        response.appendInt32(this.Group.getGuildBaseColor());
        response.appendInt32(4);
        
        response.appendInt32(this.Group.getGuildStates().get(0));
        response.appendInt32(this.Group.getGuildStates().get(1));
        response.appendInt32(this.Group.getGuildStates().get(2));
        response.appendInt32(this.Group.getGuildStates().get(3));
        response.appendInt32(this.Group.getGuildStates().get(4));
        response.appendInt32(this.Group.getGuildStates().get(5));
        response.appendInt32(this.Group.getGuildStates().get(6));
        response.appendInt32(this.Group.getGuildStates().get(7));
        response.appendInt32(this.Group.getGuildStates().get(8));
        response.appendInt32(this.Group.getGuildStates().get(9));
        response.appendInt32(this.Group.getGuildStates().get(10));
        response.appendInt32(this.Group.getGuildStates().get(11));
        
        response.appendString(this.Group.getImage());
        response.appendInt32(this.Group.getMembers().size());
        response.appendInt32(this.Group.getPetitions().size());
		return response;
	}

}
