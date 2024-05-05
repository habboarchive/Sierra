package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class RemovePowers extends ICompose {

	private int Id;
	private int UserId;

	public RemovePowers(int id, int userId) {
		super();
		this.Id = id;
		this.UserId = userId;
	}

	@Override
	public Response compose() {
        response.init(Outgoing.RemovePowers);
        response.appendInt32(this.Id);
        response.appendInt32(this.UserId);
		return response;
	}

}
