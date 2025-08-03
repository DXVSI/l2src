package org.mmocore.gameserver.ai;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;

public class GuardRuins extends Fighter
{
	public GuardRuins(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
			return false;

		GArray<NpcInstance> around = actor.getAroundNpc(500, 300);
		if (around != null && !around.isEmpty())
			for (NpcInstance npc : around)
				if (npc.isMonster() && npc.getNpcId() != 19153 && npc.getNpcId() != 19152)
					actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 300);
		return true;
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (attacker == null || attacker.isPlayable())
			return;

		super.onEvtAttacked(attacker, damage);
	}
}