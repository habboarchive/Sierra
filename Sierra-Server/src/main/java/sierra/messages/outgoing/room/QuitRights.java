package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class QuitRights extends ICompose {

	@Override
	public Response compose() {
		response.init(Outgoing.QuitRights);
		return response;
	}

}
