package ai.tauti;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;

/**
 * @author KilRoy
 */
public class KundaGuard extends Fighter
{
	public KundaGuard(NpcInstance actor)
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
		if (around != null && !around.isEmpty() && Rnd.chance(40))
			for (NpcInstance npc : around)
				if (npc.getNpcId() == 33679 || npc.getNpcId() == 33680)
					actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 300);
		return true;
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (attacker == null)
			return;

		super.onEvtAttacked(attacker, damage);
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return 2000;
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}