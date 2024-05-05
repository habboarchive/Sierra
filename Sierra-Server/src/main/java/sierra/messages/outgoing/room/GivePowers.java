package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class GivePowers extends ICompose {

	private Room Room;
	private int Id;
	private String Username;

	public GivePowers(Room room, int id, String username) {
		super();
		this.Room = room;
		this.Id = id;
		this.Username = username;
	}

	@Override
	public Response compose() {
        response.init(Outgoing.GivePowers);
        response.appendInt32(this.Room.getId());
        response.appendInt32(this.Id);
        response.appendString(this.Username);
		return response;
	}

}
