package ai.DwarvenVillageAttack;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.tables.SkillTable;

public class SwoopCannon extends DefaultAI
{
	private static final int SKILL_ID = 16023;

	public SwoopCannon(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		addTimer(1, 1000 + Rnd.get(500));
	}

	@Override
	protected void onEvtTimer(int timer_id, Object arg1, Object arg2)
	{
		super.onEvtTimer(timer_id, arg1, arg2);

		if(timer_id == 1)
		{
			if(!isActive())
				return;
			Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			addTaskBuff(getActor(), skill);
			addTimer(1, skill.getHitTime() + 10000);
		}
	}
}
