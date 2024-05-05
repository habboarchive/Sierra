package sierra.messages.outgoing.groups;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class PurchasedGuildComposer extends ICompose {

	public PurchasedGuildComposer() {
		super();
	}

	@Override
	public Response compose() {
        response.init(Outgoing.BoughtItem);
        response.appendInt32(0x1815);
        response.appendString("CREATE_GUILD");
        response.appendInt32(10);
        response.appendInt32(0);
        response.appendInt32(0);
        response.appendBoolean(true);
        response.appendInt32(0);
        response.appendInt32(2);
        response.appendBoolean(false);
		return response;
	}

}
