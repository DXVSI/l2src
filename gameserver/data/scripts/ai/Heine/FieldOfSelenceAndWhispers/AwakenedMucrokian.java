package ai.Heine.FieldOfSelenceAndWhispers;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.geodata.GeoEngine;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.utils.Location;

/**
 * - User: Mpa3uHaKaMa3e
 * - Date: 26.06.12
 * - Time: 14:05
 * - AI для нпц Contaminated Mucrokian (22655).
 * - Агрится на защитные устройства.
 * - Игнорирует атаки монстров и отбигает.
 */
public class AwakenedMucrokian extends Fighter
{
	private NpcInstance mob = null;

	public AwakenedMucrokian(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;
		if(mob == null)
		{
			GArray<NpcInstance> around = getActor().getAroundNpc(300, 300);
			if(around != null && !around.isEmpty())
				for(NpcInstance npc : around)
					if(npc.getNpcId() == 18805 || npc.getNpcId() == 18806)
						if(mob == null || getActor().getDistance3D(npc) < getActor().getDistance3D(mob))
							mob = npc;

		}
		if(mob != null)
		{
			actor.stopMove();
			actor.setRunning();
			getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, mob, 1);
			return true;
		}
		return false;
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if(actor != null && !actor.isDead())
			if(attacker != null)
				if(attacker.getNpcId() >= 22656 && attacker.getNpcId() <= 22659)
					if(Rnd.chance(25))
					{
						Location pos = Location.findPointToStay(actor, 200, 300);
						if(GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
							actor.setRunning();
						addTaskMove(pos, false);
					}
		super.onEvtAttacked(attacker, damage);
	}
}
