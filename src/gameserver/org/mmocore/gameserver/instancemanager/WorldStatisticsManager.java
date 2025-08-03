package org.mmocore.gameserver.instancemanager;

import org.mmocore.gameserver.dao.WorldStatisticDAO;
import org.mmocore.gameserver.data.xml.holder.StatuesHolder;
import org.mmocore.gameserver.idfactory.IdFactory;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.WinnerStatueInstance;
import org.mmocore.gameserver.model.worldstatistics.CategoryType;
import org.mmocore.gameserver.model.worldstatistics.CharacterStatistic;
import org.mmocore.gameserver.model.worldstatistics.CharacterStatisticElement;
import org.mmocore.gameserver.templates.StatuesSpawnTemplate;
import org.mmocore.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldStatisticsManager
{
	public static final int STATISTIC_TOP_PLAYER_LIMIT = 100;
	public static final int STATUES_TOP_PLAYER_LIMIT = 5;
	private static WorldStatisticsManager _instance;
	private final List<WinnerStatueInstance> spawnedStatues;

	private WorldStatisticsManager()
	{
		spawnedStatues = new ArrayList<>();
		spawnStatues();
	}

	public static WorldStatisticsManager getInstance()
	{
		if (_instance == null)
			_instance = new WorldStatisticsManager();
		return _instance;
	}

	private void spawnStatues()
	{
		Map<CategoryType, List<Location>> spawnLocations = StatuesHolder.getInstance().getSpawnLocations();
		List<StatuesSpawnTemplate> templates = WorldStatisticDAO.getInstance().getStatueTemplates(spawnLocations.keySet());
		for (StatuesSpawnTemplate template : templates)
		{
			List<Location> locations = spawnLocations.get(template.getCategoryType());
			for (Location loc : locations)
			{
				WinnerStatueInstance statue = new WinnerStatueInstance(IdFactory.getInstance().getNextId(), template);
				statue.setLoc(loc);
				statue.spawnMe();
				spawnedStatues.add(statue);
			}
		}
	}

	private void despawnStatues()
	{
		for (WinnerStatueInstance statue : spawnedStatues)
		{
			statue.deleteMe();
		}
		spawnedStatues.clear();
	}

	public final void updateStat(Player player, CategoryType categoryType, int subCategory, long valueAdd)
	{
		categoryType = CategoryType.getCategoryById(categoryType.getClientId(), subCategory);
		if (categoryType != null)
			WorldStatisticDAO.getInstance().updateStatisticFor(player, categoryType, valueAdd);
	}

	public void updateStat(Player player, CategoryType categoryType, long valueAdd)
	{
		updateStat(player, categoryType, 0, valueAdd);
	}

	public List<CharacterStatisticElement> getCurrentStatisticsForPlayer(int charId)
	{
		return WorldStatisticDAO.getInstance().getPersonalStatisticFor(charId);
	}

	public List<CharacterStatistic> getStatisticTop(CategoryType cat, boolean global, int limit)
	{
		return WorldStatisticDAO.getInstance().getStatisticForCategory(cat, global, limit);
	}

	public void resetMonthlyStatistic()
	{
		despawnStatues();
		WorldStatisticDAO.getInstance().recalculateWinners();
		spawnStatues();
	}

	public List<CharacterStatistic> getWinners(CategoryType categoryType, boolean global, int limit)
	{
		return WorldStatisticDAO.getInstance().getWinners(categoryType, global, limit);
	}
}
