package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.stats.Env;

public class EffectVitality extends Effect
{

	public EffectVitality(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean checkCondition()
	{
		if (!_effected.isPlayer())
			return false;

		Player p = _effected.getPlayer();

		if (p.getVitality().getItems() == 0)
			return false;

		p.getVitality().decItems();
		
		return super.checkCondition();
	}

	@Override
	public boolean onActionTime()
	{
		return true;
	}

}
