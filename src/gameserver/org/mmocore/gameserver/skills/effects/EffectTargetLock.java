package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.stats.Env;

/**
 * @author ALF
 * @date 02.08.2012
 */
public class EffectTargetLock extends Effect
{

	public EffectTargetLock(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.setLockedTarget(true);

	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.setLockedTarget(false);

	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}

}
