package ai.DwarvenVillageAttack;

import instances.MemoryOfDisaster;

import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.tables.SkillTable;

public class TeredorFirst extends DefaultAI
{
	private static final int SKILL_ID = 16021;

	public TeredorFirst(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(1, 2500);
	}

	@Override
	protected void onEvtTimer(int timer_id, Object arg1, Object arg2)
	{
		super.onEvtTimer(timer_id, arg1, arg2);
		switch(timer_id)
		{
			case 1:
				Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 2:
				Reflection r = getActor().getReflection();
				if(r instanceof MemoryOfDisaster)
					((MemoryOfDisaster) r).spawnWyrm();
				getActor().deleteMe();
				break;
		}
	}

	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if(skill_id == SKILL_ID)
			addTimer(2, 2000);
	}
}
