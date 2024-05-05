package sierra.messages.outgoing.item;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class RemoveObjectInventoryComposer extends ICompose {

	private int Id;
	
	public RemoveObjectInventoryComposer(int id) {
		super();
		this.Id = id;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.RemoveObjectFromInventory);
		response.appendInt32(this.Id);
		return response;
	}

}
