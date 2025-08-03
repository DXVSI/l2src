package org.mmocore.gameserver.skills.effects;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.stats.Env;

/**
 * @author ALF
 * @date 06.11.2012
 */
public class EffectChangeTarget extends Effect
{
	private int radius;
	private int height;

	public EffectChangeTarget(Env env, EffectTemplate template)
	{
		super(env, template);
		radius = template.getParam().getInteger("radius");
		height = template.getParam().getInteger("height");
	}

	public boolean checkCondition()
	{
		return Rnd.chance(_template.chance(100));
	}

	@Override
	public void onStart()
	{
		GArray<Creature> _around = getEffected().getAroundCharacters(radius, height);

		if (_around.isEmpty())
			return;

		_around.remove(getEffector());

		if (_around.isEmpty())
			return;

		Creature target = _around.get(Rnd.get(_around.size()));

		getEffected().setTarget(target);

	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}

	@Override
	public boolean isHidden()
	{
		return true;
	}

}
