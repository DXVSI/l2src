package ai.Heine.FieldOfSelenceAndWhispers;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.ai.CharacterAI;
import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.components.ChatType;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.scripts.Functions;

/**
 * - User: Mpa3uHaKaMa3e
 * - Date: 26.06.12
 * - Time: 21:33
 * - AI для нпц Waste Landfill Machine (18805).
 * - Если был атакован то кричит в чат и зовут на помощь.
 */
public class WastLandfillMachine extends CharacterAI
{
	private boolean _firstTimeAttacked = true;

	public WastLandfillMachine(Creature actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = (NpcInstance) getActor();
		if(actor == null || actor.isDead())
			return;

		if(_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			Functions.npcSay(actor, NpcString.ALERT_ALERT_DAMAGE_DETECTION_RECOGNIZED_COUNTERMEASURES_ENABLED, ChatType.ALL, 5000);
			GArray<NpcInstance> around = actor.getAroundNpc(1500, 300);
			if(around != null && !around.isEmpty())
				for(NpcInstance npc : around)
					if(npc.isMonster() && npc.getNpcId() == 22656 || npc.getNpcId() == 22657)
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
		}

		super.onEvtAttacked(attacker, damage);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}