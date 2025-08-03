package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.StatsSet;

import java.util.List;

public class BuffCharger extends Skill
{
	private int _target;

	public BuffCharger(StatsSet set)
	{
		super(set);
		_target = set.getInteger("targetBuff", 0);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for (Creature target : targets)
		{
			int level = 0;
			List<Effect> el = target.getEffectList().getEffectsBySkillId(_target);
			if (el != null)
				level = el.get(0).getSkill().getLevel();

			Skill next = SkillTable.getInstance().getInfo(_target, level + 1);
			if (next != null)
				next.getEffects(activeChar, target, false, false);
		}
	}
}