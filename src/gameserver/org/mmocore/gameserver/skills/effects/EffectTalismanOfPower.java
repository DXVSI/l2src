package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.stats.Env;

/**
 * @author ALF
 * @date 09.07.2012 Эффект нужен для отображения красивого эффекта на чаре.
 */
public class EffectTalismanOfPower extends Effect
{

	public EffectTalismanOfPower(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		/*
		 * switch(getSkill().getLevel()) { case 1: case 2: getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER1); break; case 3:
		 * getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER2); break; case 4:
		 * getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER3); break; case 5:
		 * getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER4); break; case 6:
		 * getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER5); break; }
		 */
	}

	@Override
	public void onExit()
	{
		super.onExit();
		/*
		 * getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER1); getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER2);
		 * getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER3); getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER4);
		 * getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER5);
		 */
	}

	@Override
	protected boolean onActionTime()
	{
		return false;
	}

}
