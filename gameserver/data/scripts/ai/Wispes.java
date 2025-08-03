package ai;


import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.MagicSkillUse;
import org.mmocore.gameserver.tables.SkillTable;


/**
 * @author ALF
 * Доработал Mangol
 * @date 30.08.2012 АИ для нпц-шариков которые хилят чаров и потом удаляютса
 */
public class Wispes extends DefaultAI {
	public Wispes (NpcInstance actor) {
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
		AI_TASK_ATTACK_DELAY = 2000;
	}
    @Override
	protected boolean thinkActive() {
		if (!_def_think) {
			NpcInstance npc = getActor();
			if (npc == null)
				return true;
            GArray<Creature> target = new GArray<Creature>();
                for (Player player : World.getAroundPlayers(npc, 300, 300)) {
                    if (player.getEffectList().getEffectsBySkillId(12001) == null) {
                        target.add(player);
                        if (npc.getNpcId() == 32915) {
                            npc.broadcastPacket(new MagicSkillUse(npc, player, 14064, 1, 0, 0));
                            npc.callSkill(SkillTable.getInstance().getInfo(14064, 1), target, true);
                        }
						else if (npc.getNpcId() == 32916) {
                            npc.broadcastPacket(new MagicSkillUse(npc, player, 14065, 1, 0, 0));
                            npc.callSkill(SkillTable.getInstance().getInfo(14065, 1), target, true);
                        }
                    }
				if (target.size() > 0) {
					target.clear();
					npc.deleteMe();
				}
        }
    }
	  return true;
    }

	@Override
	public boolean isGlobalAI() {
		return true;
	}
}