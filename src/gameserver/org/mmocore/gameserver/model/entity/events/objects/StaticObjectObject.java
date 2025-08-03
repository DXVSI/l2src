package org.mmocore.gameserver.model.entity.events.objects;

import org.mmocore.gameserver.data.xml.holder.StaticObjectHolder;
import org.mmocore.gameserver.model.entity.events.GlobalEvent;
import org.mmocore.gameserver.model.instances.StaticObjectInstance;

/**
 * @author VISTALL
 * @date 22:50/09.03.2011
 */
public class StaticObjectObject implements SpawnableObject
{
	/**
     *
     */
	private static final long serialVersionUID = -2223127864753603454L;
	private int _uid;
	private StaticObjectInstance _instance;

	public StaticObjectObject(int id)
	{
		_uid = id;
	}

	@Override
	public void spawnObject(GlobalEvent event)
	{
		_instance = StaticObjectHolder.getInstance().getObject(_uid);
	}

	@Override
	public void despawnObject(GlobalEvent event)
	{
		//
	}

	@Override
	public void refreshObject(GlobalEvent event)
	{
		if (!event.isInProgress())
			_instance.removeEvent(event);
		else
			_instance.addEvent(event);
	}

	public void setMeshIndex(int id)
	{
		_instance.setMeshIndex(id);
		_instance.broadcastInfo(false);
	}

	public int getUId()
	{
		return _uid;
	}
}
