package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.stats.Env;

public class EffectManaDamOverTime extends Effect
{
	public EffectManaDamOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean onActionTime()
	{
		if (_effected.isDead())
			return false;

		double manaDam = calc();
		if (manaDam > _effected.getCurrentMp() && getSkill().isToggle())
		{
			_effected.sendPacket(Msg.NOT_ENOUGH_MP);
			_effected.sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(getSkill().getId(), getSkill().getDisplayLevel()));
			return false;
		}

		_effected.reduceCurrentMp(manaDam, null);
		return true;
	}
}