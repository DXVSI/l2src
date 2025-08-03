package ai;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.stats.Env;
import org.mmocore.gameserver.skills.effects.EffectTemplate;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.tables.SkillTable;

public class BloodyDisciple extends Fighter
{
	private static final int[] buffId = { 14975, 14976, 14977 };
	
	public BloodyDisciple(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		if (killer.isPlayable())
		{
			Player player = killer.getPlayer();
			for(int buff : buffId)
			{
				Skill skill = SkillTable.getInstance().getInfo(buff, 1);
				for(EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					player.getEffectList().addEffect(effect);
				}
			}
		}
		super.onEvtDead(killer);
	}
}