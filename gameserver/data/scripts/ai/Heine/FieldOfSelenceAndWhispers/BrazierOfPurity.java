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
 * - Time: 16:51
 * - AI для нпц Brazier Of Purity (18806).
 * - Если был атакован то кричит в чат и зовут на помощь.
 */
public class BrazierOfPurity extends CharacterAI
{
	private boolean _firstTimeAttacked = true;

	public BrazierOfPurity(Creature actor)
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
			Functions.npcSay(actor, NpcString.THE_PURIFICATION_FIELD_IS_BEING_ATTACKED_GUARDIAN_SPIRITS_PROTECT_THE_MAGIC_FORCE, ChatType.ALL, 15000);
			GArray<NpcInstance> around = actor.getAroundNpc(1500, 300);
			if(around != null && !around.isEmpty())
				for(NpcInstance npc : around)
					if(npc.isMonster() && npc.getNpcId() == 22658 || npc.getNpcId() == 22659)
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