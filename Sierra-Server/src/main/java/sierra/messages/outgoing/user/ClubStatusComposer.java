/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>. 
 * 
 * This file is part of Sierra.
 * 
 * Sierra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sierra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sierra.  If not, see <http ://www.gnu.org/licenses/>.
 */

package sierra.messages.outgoing.user;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.habbohotel.subscription.Subscription;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;


public class ClubStatusComposer extends ICompose {

	private Subscription Subscription;
	
	public ClubStatusComposer(Subscription subscription) {
		super();
		Subscription = subscription;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.SerializeClub);
		response.appendString("club_habbo");
		
		if (this.Subscription.validSubscription())
		{
            int Expire = this.Subscription.getTimeExpire();
            int TimeLeft = Expire - Sierra.getUnixTime();
            int TotalDaysLeft = (int)Math.ceil(TimeLeft / 86400);
            int MonthsLeft = TotalDaysLeft / 31;

            if (MonthsLeft >= 1) MonthsLeft--;

            response.appendInt32(TotalDaysLeft - (MonthsLeft * 31));
            response.appendInt32(2); // ??
            response.appendInt32(MonthsLeft);
            response.appendInt32(1); // type
            response.appendBoolean(true);
            response.appendBoolean(true);
            response.appendInt32(0);
            response.appendInt32(TotalDaysLeft - (MonthsLeft * 31));
            response.appendInt32(TotalDaysLeft - (MonthsLeft * 31));
		}
		else 
		{
			if (this.Subscription.exists()) {
				this.Subscription.delete();
			}
			
            response.appendInt32(0);
            response.appendInt32(2); // ??
            response.appendInt32(0);
            response.appendInt32(1); // type
            response.appendBoolean(false);
            response.appendBoolean(true);
            response.appendInt32(0);
            response.appendInt32(0); // days i have on hc
            response.appendInt32(0); // days i have on vip
		}
		return response;
	}

}
