package sierra.habbohotel.guilds;

import java.util.HashMap;
import java.util.List;

import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;



public class Guild {

    private Integer Id;
    private String Name;
    private Integer OwnerId;
    private String OwnerName;
    private String Description;
    private Integer RoomId;
    private String Image;
    private int CustomColor1;
    private int CustomColor2;
    private int GuildBase;
    private int GuildBaseColor;
    private List<Integer> GuildStates;
    private String HtmlColor1;
    private String HtmlColor2;
	private String DateCreated;
    private List<Integer> Members;
    private List<Integer> Petitions;
    private Integer Type;
    private Integer RightsType;
    private HashMap<Integer, Integer> GuildRanks;
    private List<String> Joindates;
    
    
	public Guild(int id, String name, int ownerid, String ownername, String description, int roomid, String image, int customcolor1, int customcolor2, int guildbase, int guildbasecolor, List<Integer> guildstates, String htmlcolor1, String htmlcolor2, String datecreated, List<Integer> members, List<Integer> petitions, int type, int rightstype, HashMap<Integer,Integer> guildranks, List<String> joindates) 
	{
		Id = id;
		Name = name;
		OwnerId = ownerid;
		OwnerName = ownername;
		Description = description;
		RoomId = roomid;
		Image = image;
		CustomColor1 = customcolor1;
		CustomColor2 = customcolor2;
		GuildBase = guildbase;
		GuildBaseColor = guildbasecolor;
		GuildStates = guildstates;
		HtmlColor1 = htmlcolor1;
		HtmlColor2 = htmlcolor2;
		DateCreated = datecreated;
		Members = members;
		Petitions = petitions;
		Type = type;
		RightsType = rightstype;
		GuildRanks = guildranks;
		setJoindates(joindates);
	}
	
	public void dispose()
	{
		this.GuildRanks.clear();
		this.GuildStates.clear();
		this.Joindates.clear();
		this.Members.clear();
		this.Petitions.clear();
		
		GuildEngine.getGuilds().remove(this.Id);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getOwnerId() {
		return OwnerId;
	}

	public void setOwnerId(Integer ownerId) {
		OwnerId = ownerId;
	}

	public String getOwnerName() {
		return OwnerName;
	}

	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Integer getRoomId() {
		return RoomId;
	}

	public Room getRoom() {
		return RoomEngine.roomMap().get(RoomId);
	}

	public boolean isRoomOnline() {
		return RoomEngine.roomMap().containsKey(RoomId);
	}
	
	public void setRoomId(Integer roomId) {
		RoomId = roomId;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public int getCustomColor1() {
		return CustomColor1;
	}

	public void setCustomColor1(int customColor1) {
		CustomColor1 = customColor1;
	}

	public int getCustomColor2() {
		return CustomColor2;
	}

	public void setCustomColor2(int customColor2) {
		CustomColor2 = customColor2;
	}

	public int getGuildBase() {
		return GuildBase;
	}

	public void setGuildBase(int guildBase) {
		GuildBase = guildBase;
	}

	public int getGuildBaseColor() {
		return GuildBaseColor;
	}

	public void setGuildBaseColor(int guildBaseColor) {
		GuildBaseColor = guildBaseColor;
	}

	public List<Integer> getGuildStates() {
		return GuildStates;
	}

	public void setGuildStates(List<Integer> guildStates) {
		GuildStates = guildStates;
	}

	public String getHtmlColor1() {
		return HtmlColor1;
	}

	public void setHtmlColor1(String htmlColor1) {
		HtmlColor1 = htmlColor1;
	}

	public String getHtmlColor2() {
		return HtmlColor2;
	}

	public void setHtmlColor2(String htmlColor2) {
		HtmlColor2 = htmlColor2;
	}

	public String getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}
	
	public List<Integer> getMembers() {
		return Members;
	}

	public void setMembers(List<Integer> members) {
		Members = members;
	}

	public List<Integer> getPetitions() {
		return Petitions;
	}

	public void setPetitions(List<Integer> petitions) {
		Petitions = petitions;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer type) {
		Type = type;
	}

	public Integer getRightsType() {
		return RightsType;
	}

	public void setRightsType(Integer rightsType) {
		RightsType = rightsType;
	}

	public HashMap<Integer, Integer> getGuildRanks() {
		return GuildRanks;
	}

	public void setGuildRanks(HashMap<Integer, Integer> guildRanks) {
		GuildRanks = guildRanks;
	}

	public List<String> getJoindates() {
		return Joindates;
	}

	public void setJoindates(List<String> joindates) {
		Joindates = joindates;
	}

	public Integer getId() {
		return Id;
	}
   

}
