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

package sierra.habbohotel.allowances;

import sierra.netty.readers.Response;

public class Allowance
{
	public String Label;
	public String Level;
	public String FieldPermission;
	public Integer MinRank;
	public Boolean Override;
	public Boolean OverrideState;
	
	public Allowance(String label, String level, String fieldPermission, Integer minRank, Boolean override, Boolean overrideState) {
		this.Label = label;
		this.Level = level;
		this.FieldPermission = fieldPermission;
		this.MinRank = minRank;
		this.Override = override;
		this.OverrideState = overrideState;
	}


	public void compose(Boolean Permission, Response Message) {
		Message.appendString(this.Label);
		Message.appendBoolean(Permission);
		Message.appendString(this.Level);
	}	
}
