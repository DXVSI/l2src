package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.stats.Formulas;
import org.mmocore.gameserver.stats.Formulas.AttackInfo;
import org.mmocore.gameserver.templates.StatsSet;

public class ChargeSoul extends Skill
{
	private int _numSouls;

	public ChargeSoul(StatsSet set)
	{
		super(set);
		_numSouls = set.getInteger("numSouls", getLevel());
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if (!activeChar.isPlayer())
			return;

		boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
		if (ss && getTargetType() != SkillTargetType.TARGET_SELF)
			activeChar.unChargeShots(false);

		Creature realTarget;
		boolean reflected;

		for (Creature target : targets)
			if (target != null)
			{
				if (target.isDead())
					continue;

				reflected = target != activeChar && target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;

				if (getPower() > 0) // Если == 0 значит скилл "отключен"
				{
					AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);

					if (info.lethal_dmg > 0)
						realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);

					realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
					if (!reflected)
						realTarget.doCounterAttack(this, activeChar, false);
				}

				if (realTarget.isPlayable() || realTarget.isMonster())
					activeChar.setConsumedSouls(activeChar.getConsumedSouls() + _numSouls, null);

				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}

		if (isSSPossible())
			activeChar.unChargeShots(isMagic());
	}
}