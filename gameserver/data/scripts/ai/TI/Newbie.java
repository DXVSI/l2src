package ai.TI;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.ExShowScreenMessage;
import org.mmocore.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import org.mmocore.gameserver.network.serverpackets.MagicSkillUse;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.tables.SkillTable;

public class Newbie extends DefaultAI
{

	private final static int[][] _mageBuff = new int[][] {
			// minlevel maxlevel skill skilllevel
			{ 1, 75, 4322, 1 }, // windwalk
			{ 1, 75, 4323, 1 }, // shield
			{ 1, 75, 5637, 1 }, // Magic Barrier 1
			{ 1, 75, 4328, 1 }, // blessthesoul
			{ 1, 75, 4329, 1 }, // acumen
			{ 1, 75, 4330, 1 }, // concentration
			{ 1, 75, 4331, 1 }, // empower
			{ 16, 34, 4338, 1 }, // life cubic
	};

	private final static int[][] _warrBuff = new int[][] {
			// minlevel maxlevel skill
			{ 1, 75, 4322, 1 }, // windwalk
			{ 1, 75, 4323, 1 }, // shield
			{ 1, 75, 5637, 1 }, // Magic Barrier 1
			{ 1, 75, 4324, 1 }, // btb
			{ 1, 75, 4325, 1 }, // vampirerage
			{ 1, 75, 4326, 1 }, // regeneration
			{ 1, 39, 4327, 1 }, // haste 1
			{ 40, 75, 5632, 1 }, // haste 2
			{ 16, 34, 4338, 1 }, // life cubic
	};

	public Newbie(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
	}

	@Override
	protected boolean thinkActive()
	{
		if(!_def_think)
		{
			NpcInstance npc = getActor();
			if(npc == null)
				return true;

			for(Player player : World.getAroundPlayers(npc, 200, 200))
				if(!player.isDead() && player.getLevel() <= 40 && player.getEffectList().getEffectsBySkillId(4322) == null)
				{
					GArray<Creature> target = new GArray<Creature>();
					target.add(player);
					if(!player.isMageClass())
						for(int[] buff : _warrBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
						}
					else
						for(int[] buff : _mageBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
						}
					player.sendPacket(new ExShowScreenMessage(NpcString.NEWBIE_HELPER_HAS_CASTED_BUFFS_ON_$S1, 800, ScreenMessageAlign.TOP_CENTER, true, String.valueOf(player.getName())));
				}
		}
		return true;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}