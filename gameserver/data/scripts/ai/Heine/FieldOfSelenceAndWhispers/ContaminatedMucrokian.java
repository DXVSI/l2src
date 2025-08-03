package ai.Heine.FieldOfSelenceAndWhispers;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.CtrlIntention;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.geodata.GeoEngine;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.components.ChatType;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.utils.Location;

/**
 * - User: Mpa3uHaKaMa3e
 * - Date: 26.06.12
 * - Time: 18:16
 * - AI для нпц Contaminated Mucrokian (22654).
 * - Перед тем как броситься в атаку, кричит фразу.
 * - Игнорирует атаки монстров и отбигает.
 */
public class ContaminatedMucrokian extends Fighter
{
	public ContaminatedMucrokian(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onIntentionAttack(Creature target)
	{
		NpcInstance actor = getActor();
		if(actor == null)
			return;
		if(getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			Functions.npcSay(actor, NpcString.NAIA_WAGANAGEL_PEUTAGUN, ChatType.ALL, 5000);
		super.onIntentionAttack(target);
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