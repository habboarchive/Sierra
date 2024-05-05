package sierra.habbohotel.room;

import sierra.netty.readers.Response;

public class RoomChatlog {
	
	private String username;
    private String message;
	private int Hour;
	private int Minute;

	public RoomChatlog(String username, String message, int hour, int minute)
    {
        this.username = username;
        this.message = message;
        this.Hour =  hour;
        this.Minute = minute;
        
    }

    public void Serialize(Response packet)
    {
        packet.appendInt32(this.Hour);
        packet.appendInt32(this.Minute);
        packet.appendString(this.username);
        packet.appendString(this.message);
    }
}
