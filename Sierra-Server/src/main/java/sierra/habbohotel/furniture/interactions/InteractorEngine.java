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

package sierra.habbohotel.furniture.interactions;

import java.util.HashMap;
import java.util.Map;

public class InteractorEngine
{
	private static Map<String, Interaction> actions = new HashMap<String, Interaction>();
	
	public static void load()
	{
		actions.put("teleport", new TeleportAction());
		actions.put("vendingmachine", new VendingMachineAction());
		actions.put("default", new DefaultAction());
		actions.put("dice", new DiceAction());
		actions.put("gate", new GateAction());
	}
	
	public static Boolean hasAction(String action) {
		return actions.containsKey(action);
	}
	
	public static Interaction getAction(String action) {
		return actions.get(action);
	}
}
