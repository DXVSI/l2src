package org.mmocore.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.List;
import org.mmocore.commons.data.xml.AbstractHolder;
import org.mmocore.gameserver.templates.spawn.WalkerRouteTemplate;

public final class WalkerRoutesHolder extends AbstractHolder
{
	private static final WalkerRoutesHolder _instance = new WalkerRoutesHolder();

	private List<WalkerRouteTemplate> _spawns = new ArrayList<WalkerRouteTemplate>();

	public static WalkerRoutesHolder getInstance()
	{
		return _instance;
	}

	protected WalkerRoutesHolder()
	{
	}

	public void addSpawn(WalkerRouteTemplate spawn)
	{
		_spawns.add(spawn);
	}

	public List<WalkerRouteTemplate> getSpawns()
	{
		return _spawns;
	}

	@Override
	public int size()
	{
		return _spawns.size();
	}

	@Override
	public void clear()
	{
		_spawns.clear();
	}
}
