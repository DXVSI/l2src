package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.data.xml.holder.NpcHolder;
import org.mmocore.gameserver.idfactory.IdFactory;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.DecoyInstance;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;

/**
 * @author n0nam3
 * @date 23/07/2010 19:14
 */
public class Decoy extends Skill
{
	private final int _npcId;
	private final int _lifeTime;

	public Decoy(StatsSet set)
	{
		super(set);

		_npcId = set.getInteger("npcId", 0);
		_lifeTime = set.getInteger("lifeTime", 1200) * 1000;
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (activeChar.isAlikeDead() || !activeChar.isPlayer() || activeChar != target) // only
		                                                                                // TARGET_SELF
			return false;

		if (_npcId <= 0)
			return false;

		if (activeChar.isInObserverMode())
			return false;

		/*
		 * need correct if(activeChar.getPet() != null || activeChar.getPlayer().isMounted()) {
		 * activeChar.getPlayer().sendPacket(Msg.YOU_ALREADY_HAVE_A_PET); return false; }
		 */
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature caster, GArray<Creature> targets)
	{
		Player activeChar = caster.getPlayer();

		NpcTemplate DecoyTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
		DecoyInstance decoy = new DecoyInstance(IdFactory.getInstance().getNextId(), DecoyTemplate, activeChar, _lifeTime);

		decoy.setCurrentHp(decoy.getMaxHp(), false);
		decoy.setCurrentMp(decoy.getMaxMp());
		decoy.setHeading(activeChar.getHeading());
		decoy.setReflection(activeChar.getReflection());

		activeChar.setDecoy(decoy);

		decoy.spawnMe(Location.findAroundPosition(activeChar, 50, 70));

	}
}