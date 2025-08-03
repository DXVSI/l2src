package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Summon;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.stats.Formulas;
import org.mmocore.gameserver.templates.StatsSet;

public class DestroySummon extends Skill
{
	public DestroySummon(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for (Creature target : targets)
			if (target != null)
			{

				if (getActivateRate() > 0 && !Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
					continue;
				}

				if (target.isSummon())
				{
					((Summon) target).saveEffects();
					((Summon) target).unSummon();
					getEffects(activeChar, target, getActivateRate() > 0, false);
				}
			}

		if (isSSPossible())
			activeChar.unChargeShots(isMagic());
	}
}