package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.templates.StatsSet;

public class VitalityHeal extends Skill
{
	public VitalityHeal(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!activeChar.isPlayer())
			return false;

		Player p = target.getPlayer();

		if (p.getVitality().getItems() == 0)
			return false;

		p.getVitality().decItems();
		
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		int fullPoints = Config.MAX_VITALITY;
		double percent = _power;

		for (Creature target : targets)
		{
			if (target.isPlayer())
			{
				Player player = target.getPlayer();
				int points = (int) (fullPoints / 100 * percent);
				player.getVitality().incPoints(points);
			}
			getEffects(activeChar, target, getActivateRate() > 0, false);
		}

		if (isSSPossible())
			activeChar.unChargeShots(isMagic());
	}
}