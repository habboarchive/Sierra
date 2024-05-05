package sierra.messages.outgoing.groups;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class BuyGroupDialogComposer extends ICompose {

	private ConcurrentLinkedQueue<Room> Rooms;

	public BuyGroupDialogComposer(ConcurrentLinkedQueue<Room> rooms) {
		super();
		this.Rooms = rooms;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.BuyGroupDialog);
        response.appendInt32(10);
        response.appendInt32(this.Rooms.size());

        for (Room Room : this.Rooms) {
            response.appendInt32(Room.getId());
            response.appendString(Room.getName());
            response.appendBoolean(false);
        }
        
        response.appendInt32(5);
        response.appendInt32(10);
        response.appendInt32(3);
        response.appendInt32(4);
        response.appendInt32(25);
        response.appendInt32(17);
        response.appendInt32(5);
        response.appendInt32(25);
        response.appendInt32(17);
        response.appendInt32(3);
        response.appendInt32(29);
        response.appendInt32(11);
        response.appendInt32(4);
        response.appendInt32(0);
        response.appendInt32(0);
        response.appendInt32(0);
		return response;
	}

}
