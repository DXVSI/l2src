package org.mmocore.gameserver.skills.effects;

import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Summon;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.base.InvisibleType;
import org.mmocore.gameserver.stats.Env;

public final class EffectInvisible extends Effect
{
	private InvisibleType _invisibleType = InvisibleType.NONE;

	public EffectInvisible(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean checkCondition()
	{
		if (!_effected.isPlayer())
			return false;
		Player player = (Player) _effected;
		if (player.isInvisible())
			return false;
		if (player.getActiveWeaponFlagAttachment() != null)
			return false;
		return super.checkCondition();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		Player player = (Player) _effected;

		_invisibleType = player.getInvisibleType();

		player.setInvisibleType(InvisibleType.EFFECT);

		World.removeObjectFromPlayers(player);
	}

	@Override
	public void onExit()
	{
		super.onExit();
		Player player = (Player) _effected;
		if (!player.isInvisible())
			return;

		player.setInvisibleType(_invisibleType);

		player.broadcastUserInfo(true);
		for (Summon summon : player.getPets())
			summon.broadcastCharInfo();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}