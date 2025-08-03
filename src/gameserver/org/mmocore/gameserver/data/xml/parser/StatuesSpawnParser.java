package org.mmocore.gameserver.data.xml.parser;

import org.dom4j.Element;
import org.mmocore.commons.data.xml.AbstractFileParser;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.data.xml.holder.StatuesHolder;
import org.mmocore.gameserver.model.worldstatistics.CategoryType;
import org.mmocore.gameserver.utils.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Дмитрий
 * @modified KilRoy
 * @date 17.10.12 9:39
 */
public class StatuesSpawnParser extends AbstractFileParser<StatuesHolder>
{
	private static StatuesSpawnParser ourInstance = new StatuesSpawnParser();

	private StatuesSpawnParser()
	{
		super(StatuesHolder.getInstance());
	}

	public static StatuesSpawnParser getInstance()
	{
		return ourInstance;
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/StatuesSpawnData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "StatuesSpawnData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{

		for (Element statuesElement : rootElement.elements())
		{
			int type = Integer.parseInt(statuesElement.attributeValue("type"));
			CategoryType categoryType = CategoryType.getCategoryById(type, 0);

			List<Location> locations = new ArrayList<>();
			for (Element spawnElement : statuesElement.elements())
			{
				String[] loc = spawnElement.attributeValue("loc").split(",");
				locations.add(new Location(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])));
			}
			StatuesHolder.getInstance().addSpawnInfo(categoryType, locations);
		}
	}
}
