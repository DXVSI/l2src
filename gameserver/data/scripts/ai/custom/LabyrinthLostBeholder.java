package ai.custom;

import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.instances.ReflectionBossInstance;
import org.mmocore.gameserver.stats.Stats;
import org.mmocore.gameserver.stats.funcs.FuncSet;

/**
 * @author pchayka
 */
public class LabyrinthLostBeholder extends Fighter
{

	public LabyrinthLostBeholder(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		Reflection r = actor.getReflection();
		if(!r.isDefault())
			if(checkMates(actor.getNpcId()))
				if(findLostCaptain() != null)
					findLostCaptain().addStatFunc(new FuncSet(Stats.MAGIC_DEFENCE, 0x30, this, findLostCaptain().getTemplate().getBaseMDef() * 0.66));
		super.onEvtDead(killer);
	}

	private boolean checkMates(int id)
	{
		for(NpcInstance n : getActor().getReflection().getNpcs())
			if(n.getNpcId() == id && !n.isDead())
				return false;
		return true;
	}

	private NpcInstance findLostCaptain()
	{
		for(NpcInstance n : getActor().getReflection().getNpcs())
			if(n instanceof ReflectionBossInstance)
				return n;
		return null;
	}
}