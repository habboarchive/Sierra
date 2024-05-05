package sierra.messages.outgoing.groups;

import sierra.habbo.Session;
import sierra.habbohotel.guilds.Guild;
import sierra.habbohotel.headers.Outgoing;
import sierra.messages.ICompose;
import sierra.netty.readers.Response;

public class GroupInformationComposer extends ICompose {

	public Session session;
	public Guild guild;
	public Boolean flag;

	public GroupInformationComposer(Session session, Guild guild, boolean flag) {
		super();
		this.session = session;
		this.guild = guild;
		this.flag = flag;
	}

	@Override
	public Response compose() {
		response.init(Outgoing.SendAdvGroupInit);
		response.appendInt32(guild.getId());
		response.appendBoolean(true);
		response.appendInt32(0);//guild.Type);
		response.appendString(guild.getName());
		response.appendString(guild.getDescription());
		response.appendString(guild.getImage());
		response.appendInt32(guild.getOwnerId());
		response.appendString(guild.getOwnerName());
		if (guild.getPetitions().contains(session.getHabbo().Id))
		{
			response.appendInt32(2);
		}
		else if (!session.getHabbo().GroupMemberships.contains(guild.getId()))
		{
			response.appendInt32(0);
		}
		else if (session.getHabbo().GroupMemberships.contains(guild.getId()))
		{
			response.appendInt32(1);
		}
		
		response.appendInt32(guild.getMembers().size());
		response.appendBoolean(false);
		response.appendString(guild.getDateCreated());

		response.appendBoolean(this.session.getHabbo().Id == guild.getOwnerId());

		if (session.getHabbo().GroupMemberships.contains(guild.getId()))
		{
			if (guild.getGuildRanks().get(session.getHabbo().Id) < 2)
			{
				response.appendBoolean(true);
			}
			else
			{response.appendBoolean(false);

			}
		}
		else 
		{
			response.appendBoolean(false);
		}

		response.appendString(guild.getOwnerName());
		
		if (!flag)
		{
			response.appendBoolean(false);
			response.appendBoolean(false);
			response.appendInt32(guild.getPetitions().size());
		}
		else
		{
			response.appendBoolean(true);
			response.appendBoolean(true);
			response.appendInt32(guild.getMembers().contains(this.session.getHabbo().Id) ? guild.getPetitions().size() : 0);
		}

		return response;
	}

}
