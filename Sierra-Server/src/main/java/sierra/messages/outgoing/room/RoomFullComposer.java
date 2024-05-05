package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class RoomFullComposer extends ICompose {

	public RoomFullComposer() {
		super();
	}

	@Override
	public Response compose() {
		response.init(Outgoing.RoomFullMessage);
		response.appendInt32(1);
		response.appendString("/x363");
		return response;
	}

}
