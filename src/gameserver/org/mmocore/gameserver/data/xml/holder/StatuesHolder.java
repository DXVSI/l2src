package org.mmocore.gameserver.data.xml.holder;

import org.mmocore.commons.data.xml.AbstractHolder;
import org.mmocore.gameserver.model.worldstatistics.CategoryType;
import org.mmocore.gameserver.utils.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatuesHolder extends AbstractHolder
{
	private static StatuesHolder _instance;
	private Map<CategoryType, List<Location>> spawnLocations;

	private StatuesHolder()
	{
		spawnLocations = new HashMap<>();
	}

	public static StatuesHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new StatuesHolder();
		}
		return _instance;
	}

	@Override
	public int size()
	{
		return spawnLocations.size();
	}

	@Override
	public void clear()
	{
		spawnLocations.clear();
	}

	public void addSpawnInfo(CategoryType categoryType, List<Location> locations)
	{
		spawnLocations.put(categoryType, locations);
	}

	public Map<CategoryType, List<Location>> getSpawnLocations()
	{
		return Collections.unmodifiableMap(spawnLocations);
	}
}
