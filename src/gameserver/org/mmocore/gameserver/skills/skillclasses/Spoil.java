package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.MonsterInstance;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;
import org.mmocore.gameserver.stats.Formulas;
import org.mmocore.gameserver.stats.Formulas.AttackInfo;
import org.mmocore.gameserver.templates.StatsSet;

public class Spoil extends Skill
{
	public Spoil(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if (!activeChar.isPlayer())
			return;

		int ss = isSSPossible() ? isMagic() ? activeChar.getChargedSpiritShot() : activeChar.getChargedSoulShot() ? 2 : 0 : 0;
		if (ss > 0 && getPower() > 0)
			activeChar.unChargeShots(false);

		for (Creature target : targets)
			if (target != null && !target.isDead())
			{
				if (target.isMonster())
					if (((MonsterInstance) target).isSpoiled())
						activeChar.sendPacket(Msg.ALREADY_SPOILED);
					else
					{
						MonsterInstance monster = (MonsterInstance) target;
						boolean success;
						if (!Config.ALT_SPOIL_FORMULA)
						{
							int monsterLevel = monster.getLevel();
							int modifier = Math.abs(monsterLevel - activeChar.getLevel());
							double rateOfSpoil = Config.BASE_SPOIL_RATE;

							if (modifier > 8)
								rateOfSpoil = rateOfSpoil - rateOfSpoil * (modifier - 8) * 9 / 100;

							rateOfSpoil = rateOfSpoil * getMagicLevel() / monsterLevel;

							if (rateOfSpoil < Config.MINIMUM_SPOIL_RATE)
								rateOfSpoil = Config.MINIMUM_SPOIL_RATE;
							else if (rateOfSpoil > 99.)
								rateOfSpoil = 99.;

							if (((Player) activeChar).isGM())
								activeChar.sendMessage(new CustomMessage("org.mmocore.gameserver.skills.skillclasses.Spoil.Chance", (Player) activeChar).addNumber((long) rateOfSpoil));
							success = Rnd.chance(rateOfSpoil);
						}
						else
							success = Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate());

						if (success && monster.setSpoiled((Player) activeChar))
							activeChar.sendPacket(Msg.THE_SPOIL_CONDITION_HAS_BEEN_ACTIVATED);
						else
							activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_FAILED).addSkillName(_id, getDisplayLevel()));
					}

				if (getPower() > 0)
				{
					double damage, reflectableDamage = 0;
					if (isMagic())
					{
						AttackInfo info = Formulas.calcMagicDam(activeChar, target, this, ss);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
					}
					else
					{
						AttackInfo info = Formulas.calcPhysDam(activeChar, target, this, false, false, ss > 0, false);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
						if (info.lethal_dmg > 0)
							target.reduceCurrentHp(info.lethal_dmg, reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
					}

					target.reduceCurrentHp(damage, reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
					target.doCounterAttack(this, activeChar, false);
				}

				getEffects(activeChar, target, false, false);

				target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, Math.max(_effectPoint, 1));
			}
	}
}