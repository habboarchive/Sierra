package sierra.messages.outgoing.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class UnseenItemsUpdate extends ICompose {

	private Map<Integer, Integer> Items;

	public UnseenItemsUpdate(Map<Integer, Integer> itemIds) {
		super();
		this.Items = itemIds;
	}


	@Override
	public Response compose() {
		response.init(Outgoing.UnseenItems);
		response.appendInt32(this.Items.size());
		
		for (Entry<Integer, Integer> set : this.Items.entrySet()) {
			response.appendInt32(set.getValue());
			response.appendInt32(1); // should this item alert be enabled? 1 for true, 0 for false
			response.appendInt32(set.getKey());
		}
		
		return response;
	}

}
