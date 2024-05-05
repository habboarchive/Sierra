package sierra.habbohotel.guilds;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;



public class GuildsPartsDataEngine
{
	private static List<GuildsPartsData> BaseBadges;
	private static List<GuildsPartsData> ColorBadges1;
	private static List<GuildsPartsData> ColorBadges2;
	private static List<GuildsPartsData> ColorBadges3;
	private static List<GuildsPartsData> SymbolBadges;

	static {
		BaseBadges = new ArrayList<GuildsPartsData>();
		SymbolBadges = new ArrayList<GuildsPartsData>();
		ColorBadges1 = new ArrayList<GuildsPartsData>();
		ColorBadges2 = new ArrayList<GuildsPartsData>();
		ColorBadges3 = new ArrayList<GuildsPartsData>();
	}

	public static void load()
	{
		try

		{
			ResultSet row = Sierra.getStorage().queryParams("SELECT * FROM groups_elements;").executeQuery();

			while (row.next())
			{

				GuildsPartsData data;
				
				if (row.getString("Type").equals("Base"))
				{
					data = new GuildsPartsData();

					data.setId(row.getInt("Id"));
					data.setExtraData1(row.getString("ExtraData1"));
					data.setExtraData2(row.getString("ExtraData2"));

					BaseBadges.add(data);
				}
				else if (row.getString("ExtraData1").startsWith("symbol_"))
				{
					data = new GuildsPartsData();

					data.setId(row.getInt("Id"));
					data.setExtraData1(row.getString("ExtraData1"));
					data.setExtraData2(row.getString("ExtraData2"));

					SymbolBadges.add(data);
				}
				else if (row.getString("Type").equals("Color1"))
				{
					data = new GuildsPartsData();

					data.setId(row.getInt("Id"));
					data.setExtraData1(row.getString("ExtraData1"));
					data.setExtraData2(row.getString("ExtraData2"));


					ColorBadges1.add(data);
				}
				else if (row.getString("Type").equals("Color2"))
				{
					data = new GuildsPartsData();

					data.setId(row.getInt("Id"));
					data.setExtraData1(row.getString("ExtraData1"));
					data.setExtraData2(row.getString("ExtraData2"));


					ColorBadges2.add(data);
				}
				else if (row.getString("Type").equals("Color3"))
				{
					data = new GuildsPartsData();

					data.setId(row.getInt("Id"));
					data.setExtraData1(row.getString("ExtraData1"));
					data.setExtraData2(row.getString("ExtraData2"));


					ColorBadges3.add(data);
				}
			}
		}
		catch (Exception e)
		{

		}
	}

	public static List<GuildsPartsData> getBaseBadges() {
		return BaseBadges;
	}

	public static List<GuildsPartsData> getColorBadges1() {
		return ColorBadges1;
	}

	public static List<GuildsPartsData> getColorBadges2() {
		return ColorBadges2;
	}

	public static List<GuildsPartsData> getColorBadges3() {
		return ColorBadges3;
	}

	public static List<GuildsPartsData> getSymbolBadges() {
		return SymbolBadges;
	}

}
