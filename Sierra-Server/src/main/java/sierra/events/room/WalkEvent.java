package sierra.events.room;

import sierra.events.IEvent;
import sierra.habbo.Session;

public class WalkEvent extends IEvent 
{
	private int ToX;
	private int ToY;
	private int FromX;
	private int FromY;

	public WalkEvent(Session session, int GoalX, int GoalY) 
	{
		super(session);
		this.ToX = GoalX;
		this.ToY = GoalY;
		
		this.FromX = session.getRoomUser().getX();
		this.FromY = session.getRoomUser().getY();
	}


	public void teleportTo(int X, int Y) {
		Session.getRoomUser().teleportTo(X, Y);
	}
	
	public int getToX() {
		return ToX;
	}

	public int getToY() {
		return ToY;
	}

	public int getFromX() {
		return FromX;
	}

	public int getFromY() {
		return FromY;
	}

}
