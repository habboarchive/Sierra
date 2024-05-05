package sierra.messages.incoming.room;

import sierra.messages.IMessage;
import sierra.messages.outgoing.room.EndTalentPractiseComposer;
import sierra.messages.outgoing.room.LoadQuizComposer;
import sierra.messages.outgoing.user.AllowanceComposer;

public class StartQuiz extends IMessage {

	public String Quiz;
	
	@Override
	public void handle() throws Exception {
		
		if (Quiz.equals("SafetyQuiz1"))
		{
			session.getHabbo().Quiz = true;
			session.getHabbo().save();
			
			session.sendResponse(new EndTalentPractiseComposer());
			session.sendResponse(new AllowanceComposer(session));
		}
		else
		{
			session.sendResponse(new LoadQuizComposer("SafetyChat1"));
		}
		
	}

}
