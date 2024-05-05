package sierra.messages.outgoing.groups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import sierra.Sierra;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;



public class GuildHTMLColoursComposer extends ICompose {

	public List<Integer> Memberships;

	public GuildHTMLColoursComposer(List<Integer> memberships) {
		super();
		this.Memberships = memberships;
	}

	@Override
	public Response compose()  {
		response.init(Outgoing.HTMLColours);
		response.appendInt32(this.Memberships.size());

		try
		{
			for (int id : this.Memberships)
			{
				ResultSet row = Sierra.getStorage().readRow("SELECT * FROM groups WHERE id = '" + id + "'");
				response.appendInt32(row.getInt("id"));
				response.appendString(row.getString("name"));
				response.appendString(row.getString("image"));
				response.appendString(row.getString("html_color1"));
				response.appendString(row.getString("html_color2"));
				response.appendBoolean(false);//id == Session.GetHabbo().FavoriteGuild);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return response;
	}

}
