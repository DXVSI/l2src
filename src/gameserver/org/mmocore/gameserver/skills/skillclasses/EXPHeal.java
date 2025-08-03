package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.templates.StatsSet;

/**
 * @author cruel
 */
public class EXPHeal extends Skill
{
	public EXPHeal(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(final Creature activeChar, final Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!activeChar.isPlayer())
			return false;
		
		Player player = activeChar.getPlayer();
		if (!player.getActiveSubClass().isDouble() || player.getActiveSubClass().getLevel() < 75)
			return false;

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if (!activeChar.isPlayer())
			return;
		
		for (Creature target : targets)
			if (target != null)
			{
				target.getPlayer().addExpAndSp((long) _power, 0);

				getEffects(activeChar, target, getActivateRate() > 0, false);
			}

		if (isSSPossible())
			activeChar.unChargeShots(isMagic());
	}
}
