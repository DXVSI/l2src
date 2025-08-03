package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.stats.Env;

public class EffectNegateEffects extends Effect
{
	public EffectNegateEffects(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onExit()
	{
		super.onExit();
	}

	@Override
	public boolean onActionTime()
	{
		for (Effect e : _effected.getEffectList().getAllEffects())
			if (!e.getStackType().contains(EffectTemplate.NO_STACK))
				for (String arg : getStackType())
					if (e.getStackType().contains(arg))
						if (e.getStackOrder() <= getStackOrder())
							e.exit();
		return false;
	}
}