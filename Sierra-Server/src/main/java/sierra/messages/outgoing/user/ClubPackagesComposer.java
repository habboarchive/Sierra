package sierra.messages.outgoing.user;

import java.util.Calendar;
import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.shop.items.ShopItem;
import sierra.habbohotel.shop.items.ShopItemEngine;
import sierra.habbohotel.shop.pages.ShopPage;
import sierra.habbohotel.shop.pages.ShopPageEngine;
import sierra.habbohotel.subscription.Subscription;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class ClubPackagesComposer extends ICompose {

	private List<ShopItem> Items;
	private Subscription Subscription;
	
	public ClubPackagesComposer(Subscription subscription) {
		super();
		this.Items = ShopItemEngine.getItems(7);
		Subscription = subscription;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.ClubComposer);
        response.appendInt32(this.Items.size());
        
        for (ShopItem item : this.Items)
        {
        	response.appendInt32(item.Id);
            response.appendString(item.Name);
            response.appendInt32(item.Credits);
            response.appendBoolean(false);
            
            int Days = 0;
            int Months = item.Amount;
            
            Calendar now = Calendar.getInstance();
            
            if (this.Subscription.validSubscription())
            {
                double expireTime = this.Subscription.getTimeExpire();
                double TimeLeft = expireTime - Sierra.getUnixTime();
                int TotalDaysLeft = (int) Math.ceil((double) (TimeLeft / 86400.0));
                now.add(Calendar.DAY_OF_MONTH, TotalDaysLeft);
            }
            now.add(Calendar.DAY_OF_MONTH, Days);
            
            response.appendInt32(2);//0
            response.appendInt32(Months);
            response.appendInt32(Months);
            response.appendInt32(Days);
            response.appendInt32(Days);
            response.appendInt32(now.get(Calendar.YEAR));
            response.appendInt32(now.get(Calendar.MONTH));
            response.appendInt32(now.get(Calendar.DAY_OF_MONTH));
          
        }
        
        response.appendInt32(1);
        
		return response;
	}

}
