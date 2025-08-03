package ai.TI;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.CtrlIntention;
import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;

/**
 * @author KilRoy
 * Guard use, for attacking Maneken.
 */
public class GuardManeken extends DefaultAI
{
	private int Manaken = 33023;

	public GuardManeken(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 5000;
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();

		if(target.getNpcId() != Manaken)
			return false;

		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}

		return super.checkAggression(target);
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}