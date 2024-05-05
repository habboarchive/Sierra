package sierra.habbohotel.subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.messages.outgoing.handshake.FuserightComposer;



public class Subscription {

	private int timeExpire;
	private Session session;
	private boolean exists;

	public Subscription(Session session) 
	{
		this.session = session;
	}

	public boolean validSubscription() 
	{
		if (this.timeExpire <= Sierra.getUnixTime())
		{
			return false;
		}

		return true;
	}

	public boolean exists() 
	{
		return exists;
	}

	public void addOrExtendSubscription(int days) 
	{
		int seconds = days * 24 * 3600;
		
		try
		{
			if (this.exists)
			{
				// update club
				PreparedStatement Statement = Sierra.getStorage().queryParams("UPDATE members_subscriptions SET timestamp_expire = ? WHERE user_id = ?");
				{
					Statement.setInt(1, this.timeExpire + seconds);
					Statement.setInt(2, session.getHabbo().Id);
					Statement.executeUpdate();
				}
				
				this.timeExpire = this.timeExpire + seconds;
			}
			else 
			{
				// new purchase
				PreparedStatement Statement = Sierra.getStorage().queryParams("INSERT INTO members_subscriptions (`user_id`, `subscription_id`, `timestamp_activated`, `timestamp_expire`) VALUES (?, ?, ?, ?);");
				{
					Statement.setInt(1, session.getHabbo().Id);
					Statement.setString(2, "habbo_club");
					Statement.setInt(3, Sierra.getUnixTime());
					Statement.setInt(4, Sierra.getUnixTime() + seconds);
					Statement.executeUpdate();
				}
				
				this.exists = true;
				this.timeExpire =  Sierra.getUnixTime() + seconds;
			}
			
			session.sendResponse(new FuserightComposer(session.getSubscription().validSubscription(), session.getHabbo().Rank));
			
			Sierra.getSocketFactory().getMessageHandler().invokePacket(session, null, sierra.messages.incoming.user.ClubStatus.class);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void loadSubscription() 
	{
		try
		{
			ResultSet row = Sierra.getStorage().readRow("SELECT * FROM members_subscriptions WHERE user_id = '" + this.session.getHabbo().Id + "'");

			if (row != null)
			{
				this.exists = true;
				this.timeExpire = row.getInt("timestamp_expire");
			}
			else
			{
				this.exists = false;
				this.timeExpire = 0;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void delete() {
		
		this.exists = false;
		this.timeExpire = 0;
		
		session.sendResponse(new FuserightComposer(session.getSubscription().validSubscription(), session.getHabbo().Rank));
		
		Sierra.getStorage().executeQuery("DELETE FROM members_subscription WHERE user_id = '" + this.session.getHabbo().Id + "'");
	}
	
	public int getTimeExpire() {
		return timeExpire;
	}

	public void dispose() {
		// dunno lolz
	}
}
