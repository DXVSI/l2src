package org.mmocore.gameserver.model.instances;

import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

public class ChestInstance extends MonsterInstance
{
	/**
     *
     */
	private static final long serialVersionUID = 2516211559188406776L;

	public ChestInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void tryOpen(Player opener, Skill skill)
	{
		getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, opener, 100);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}