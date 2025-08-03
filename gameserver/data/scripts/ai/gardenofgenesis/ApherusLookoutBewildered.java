package ai.gardenofgenesis;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.utils.Location;
import org.mmocore.gameserver.utils.NpcUtils;

public class ApherusLookoutBewildered extends Fighter
{
	public ApherusLookoutBewildered(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		NpcInstance actor = getActor();
		if(actor != null && killer != null && actor != killer)
		{
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19001, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
		}
	}
}