package ai.Heine.FieldOfSelenceAndWhispers;

import org.mmocore.commons.util.Rnd;
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
 * - Time: 20:52
 * - AI для нпц Mucrokian (22650, 22651, 22652, 22653).
 * - Игнорирует атаки монстров и отбигает.
 * - Еесли был атакован то кричит в чат.
 */
public class Mucrokian extends Fighter
{
	public static final NpcString[] MsgText = { NpcString.PEUNGLUI_MUGLANEP_NAIA_WAGANAGEL_PEUTAGUN, NpcString.PEUNGLUI_MUGLANEP };

	public Mucrokian(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if(actor != null && !actor.isDead())
		{
			if(attacker != null)
				if(attacker.getNpcId() >= 22656 && attacker.getNpcId() <= 22659)
				{
					if(Rnd.chance(25))
					{
						Location pos = Location.findPointToStay(actor, 200, 300);
						if(GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
							actor.setRunning();
						addTaskMove(pos, false);
					}
					if(Rnd.chance(15))
						Functions.npcSay(actor, Rnd.get(MsgText), ChatType.ALL, 5000);
				}
			super.onEvtAttacked(attacker, damage);
		}
	}
}