package org.mmocore.gameserver.skills.effects;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.data.xml.holder.NpcHolder;
import org.mmocore.gameserver.geodata.GeoEngine;
import org.mmocore.gameserver.idfactory.IdFactory;
import org.mmocore.gameserver.model.*;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.instances.SymbolInstance;
import org.mmocore.gameserver.network.serverpackets.MagicSkillLaunched;
import org.mmocore.gameserver.stats.Env;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EffectSymbol extends Effect
{
	private static final Logger _log = LoggerFactory.getLogger(EffectSymbol.class);

	private NpcInstance _symbol = null;

	public EffectSymbol(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean checkCondition()
	{
		if (getSkill().getTargetType() != Skill.SkillTargetType.TARGET_SELF)
		{
			_log.error("Symbol skill with target != self, id = " + getSkill().getId());
			return false;
		}

		Skill skill = getSkill().getFirstAddedSkill();
		if (skill == null)
		{
			_log.error("Not implemented symbol skill, id = " + getSkill().getId());
			return false;
		}

		return super.checkCondition();
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Skill skill = getSkill().getFirstAddedSkill();

		// Затычка, в клиенте они почему-то не совпадают.
		skill.setMagicType(getSkill().getMagicType());

		Location loc = _effected.getLoc();
		if (_effected.isPlayer() && ((Player) _effected).getGroundSkillLoc() != null)
		{
			loc = ((Player) _effected).getGroundSkillLoc();
			((Player) _effected).setGroundSkillLoc(null);
		}

		NpcTemplate template = NpcHolder.getInstance().getTemplate(_skill.getSymbolId());
		if (getTemplate()._count <= 1)
			_symbol = new SymbolInstance(IdFactory.getInstance().getNextId(), template, _effected, skill);
		else
			_symbol = new NpcInstance(IdFactory.getInstance().getNextId(), template);

		_symbol.setLevel(_effected.getLevel());
		_symbol.setReflection(_effected.getReflection());
		_symbol.spawnMe(loc);
	}

	@Override
	public void onExit()
	{
		super.onExit();

		if (_symbol != null && _symbol.isVisible())
			_symbol.deleteMe();

		_symbol = null;
	}

	@Override
	public boolean onActionTime()
	{
		if (getTemplate()._count <= 1)
			return false;

		Creature effector = getEffector();
		Skill skill = getSkill().getFirstAddedSkill();
		NpcInstance symbol = _symbol;
		double mpConsume = getSkill().getMpConsume();

		if (effector == null || skill == null || symbol == null)
			return false;

		if (mpConsume > effector.getCurrentMp())
		{
			effector.sendPacket(Msg.NOT_ENOUGH_MP);
			return false;
		}

		effector.reduceCurrentMp(mpConsume, effector);

		// Использовать разрешено только скиллы типа TARGET_ONE
		for (Creature cha : World.getAroundCharacters(symbol, getSkill().getSkillRadius(), 200))
			if (!cha.isDoor() && cha.getEffectList().getEffectsBySkill(skill) == null && skill.checkTarget(effector, cha, cha, false, false) == null)
			{
				if (skill.isOffensive() && !GeoEngine.canSeeTarget(symbol, cha, false))
					continue;
				GArray<Creature> targets = new GArray<Creature>(1);
				targets.add(cha);
				effector.callSkill(skill, targets, true);
				effector.broadcastPacket(new MagicSkillLaunched(symbol.getObjectId(), getSkill().getDisplayId(), getSkill().getDisplayLevel(), cha));
			}

		return true;
	}
}