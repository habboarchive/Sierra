package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class DisposeFigureComposer extends ICompose {

	private int VirtualId;
	
	public DisposeFigureComposer(int virtualId) {
		super();
		VirtualId = virtualId;
	}


	@Override
	public Response compose() {
		response.init(Outgoing.LeaveRoom);
		response.appendString(this.VirtualId);
		return response;
	}

}
