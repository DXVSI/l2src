package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.templates.StatsSet;

public class EffectsFromSkills extends Skill
{
	public EffectsFromSkills(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for (Creature target : targets)
			if (target != null)
				for (AddedSkill as : getAddedSkills())
					as.getSkill().getEffects(activeChar, target, false, false);
	}
}