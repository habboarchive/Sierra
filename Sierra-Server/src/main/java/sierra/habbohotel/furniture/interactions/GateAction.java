package sierra.habbohotel.furniture.interactions;

import sierra.habbo.Session;
import sierra.habbohotel.room.items.floor.FloorItem;

public class GateAction implements Interaction {

	@Override
	public void Interact(Boolean State, Session Session, FloorItem Item) {
		
		if (Item.getExtraData().equals("0"))
		{
			Item.setExtraData("1");
			Item.saveExtraData();
			Item.sendUpdate(Session);
			return;
		}

		if (Item.getExtraData().equals("1"))
		{
			Item.setExtraData("0");
			Item.saveExtraData();
			Item.sendUpdate(Session);
			return;
		}
		
	}

}
