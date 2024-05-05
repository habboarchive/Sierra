package sierra.messages.incoming.groups;

import sierra.habbohotel.guilds.GuildsPartsData;
import sierra.habbohotel.guilds.GuildsPartsDataEngine;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class GuildElementComposer extends ICompose {

	public GuildElementComposer() {
		super();
	}

	@Override
	public Response compose() {
		
		response.init(Outgoing.GuildElements);
		response.appendInt32(GuildsPartsDataEngine.getBaseBadges().size());
		
        for (GuildsPartsData data : GuildsPartsDataEngine.getBaseBadges())
        {
            response.appendInt32(data.getId());
            response.appendString(data.getExtraData1());
            response.appendString(data.getExtraData2());
        }
        
        response.appendInt32(GuildsPartsDataEngine.getSymbolBadges().size());
        for (GuildsPartsData data : GuildsPartsDataEngine.getSymbolBadges())
        {
            response.appendInt32(data.getId());
            response.appendString(data.getExtraData1());
            response.appendString(data.getExtraData2());
        }
        
        response.appendInt32(GuildsPartsDataEngine.getColorBadges1().size());
        for (GuildsPartsData data : GuildsPartsDataEngine.getColorBadges1())
        {
            response.appendInt32(data.getId());
            response.appendString(data.getExtraData1());
        }

        response.appendInt32(GuildsPartsDataEngine.getColorBadges2().size());
        for (GuildsPartsData data : GuildsPartsDataEngine.getColorBadges2())
        {
            response.appendInt32(data.getId());
            response.appendString(data.getExtraData1());
        }
        
        response.appendInt32(GuildsPartsDataEngine.getColorBadges3().size());
        for (GuildsPartsData data : GuildsPartsDataEngine.getColorBadges3())
        {
            response.appendInt32(data.getId());
            response.appendString(data.getExtraData1());
        }
		
		return response;
	}

}
