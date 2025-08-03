package ai.GuillotineFortress;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.ExShowScreenMessage;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.utils.NpcUtils;

public class GuillotineMonsters extends Fighter
{
	private boolean _chekerLocked = true;
	public GuillotineMonsters(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		NpcInstance actor = getActor();
		addTaskBuff(actor, SkillTable.getInstance().getInfo(15208, 9));
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);
		NpcInstance actor = getActor();
		Player player = (Player) attacker;
		
		double actor_hp_precent = actor.getCurrentHpPercents();
		if(actor_hp_precent < 85 && _chekerLocked)
		{
			_chekerLocked = false;
			actor.getEffectList().stopEffect(SkillTable.getInstance().getInfo(15208, 9));
			if(attacker.isPlayer())
				player.sendPacket(new ExShowScreenMessage(NpcString.CHAOS_SHIELD_BREAKTHROUGH, 10000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
			
		if (player.getInventory().getItemByItemId(34898) != null && Rnd.chance(1))
		{
			NpcUtils.spawnSingle(23212, player.getLoc(), player.getReflection());
			player.getInventory().destroyItemByItemId(34898, 1);
		}
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		if(killer != null && killer.isPlayer() && Rnd.chance(4))
		{
			Player player = (Player) killer;
			player.getInventory().addItem(34898, 1);
		}
	}
}