package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.network.serverpackets.FinishRotating;
import org.mmocore.gameserver.network.serverpackets.StartRotating;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.stats.Formulas;
import org.mmocore.gameserver.stats.Formulas.AttackInfo;
import org.mmocore.gameserver.templates.StatsSet;

public class PDam extends Skill
{
	private final boolean _onCrit;
	private final boolean _directHp;
	private final boolean _turner;
	private final boolean _blow;

	public PDam(StatsSet set)
	{
		super(set);
		_onCrit = set.getBool("onCrit", false);
		_directHp = set.getBool("directHp", false);
		_turner = set.getBool("turner", false);
		_blow = set.getBool("blow", false);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		boolean ss = activeChar.getChargedSoulShot() && isSSPossible();

		Creature realTarget;
		boolean reflected;

		for (Creature target : targets)
			if (target != null && !target.isDead())
			{
				if (_turner && !target.isInvul())
				{
					target.broadcastPacket(new StartRotating(target, target.getHeading(), 1, 65535));
					target.broadcastPacket(new FinishRotating(target, activeChar.getHeading(), 65535));
					target.setHeading(activeChar.getHeading());
					target.sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(_displayId, _displayLevel));
				}

				reflected = target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;

				AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, _blow, ss, _onCrit);

				if (info.lethal_dmg > 0)
					realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);

				if (!info.miss || info.damage >= 1)
					realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, info.lethal ? false : _directHp, true, false, false, getPower() != 0);

				if (!reflected)
					realTarget.doCounterAttack(this, activeChar, _blow);

				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}

		if (isSuicideAttack())
			activeChar.doDie(null);
		else if (isSSPossible())
			activeChar.unChargeShots(isMagic());
	}
}