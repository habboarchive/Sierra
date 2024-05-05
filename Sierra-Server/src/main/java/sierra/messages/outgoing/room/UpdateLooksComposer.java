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

package sierra.messages.outgoing.room;

import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class UpdateLooksComposer extends ICompose {

	private int UserId;
	private String Figure;
	private String Sex;
	private String Motto;
	
	public UpdateLooksComposer(int userId, String figure, String sex, String motto) {
		super();
		this.UserId = userId;
		this.Figure = figure;
		this.Sex = sex;
		this.Motto = motto;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.UpdateInfo);
		response.appendInt32(this.UserId);
		response.appendString(this.Figure);
		response.appendString(this.Sex.toLowerCase());
		response.appendString(this.Motto);
		response.appendInt32(0); //TODO: Achievement points
		return response;
	}

}
