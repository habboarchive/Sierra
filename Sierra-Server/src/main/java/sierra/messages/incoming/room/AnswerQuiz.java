/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>session.getResponse(). 
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

package sierra.messages.incoming.room;

import sierra.messages.IMessage;
import sierra.messages.outgoing.room.EndTalentPractiseComposer;
import sierra.messages.outgoing.room.LoadQuizComposer;
import sierra.messages.outgoing.user.AllowanceComposer;

public class AnswerQuiz extends IMessage {

	public String Status;

	@Override
	public void handle() throws Exception {
		
		if (this.Status.equals("SafetyQuiz1"))
		{
			session.getHabbo().Quiz = true;
			session.getHabbo().save();
			
            session.sendResponse(new EndTalentPractiseComposer());
            session.sendResponse(new AllowanceComposer(session));
		}
		else
		{
            session.sendResponse(new LoadQuizComposer("SafetyQuiz1"));
		}
	}

}
