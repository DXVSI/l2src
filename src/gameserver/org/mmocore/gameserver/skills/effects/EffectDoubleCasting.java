package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.stats.Env;

/**
 * @author ALF
 * @date 27.07.2012
 */
public class EffectDoubleCasting extends Effect
{

	protected EffectDoubleCasting(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		if (_effected.isDoubleCast())
			return;

		_effected.setDoubleCast(true);
	}

	@Override
	public void onExit()
	{
		_effected.setDoubleCast(false);
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}

}
