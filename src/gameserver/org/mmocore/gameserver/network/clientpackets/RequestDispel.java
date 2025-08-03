package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill.SkillType;
import org.mmocore.gameserver.skills.EffectType;

public class RequestDispel extends L2GameClientPacket
{
	private int _objectId, _id, _level;

	@Override
	protected void readImpl() throws Exception
	{
		_objectId = readD();
		_id = readD();
		_level = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null || activeChar.getObjectId() != _objectId && activeChar.getPets() == null)
			return;

		Creature target = activeChar;
		if (activeChar.getObjectId() != _objectId)
			target = activeChar.getFirstPet();

		for (Effect e : target.getEffectList().getAllEffects())
			if (e.getDisplayId() == _id && e.getDisplayLevel() == _level)
				if (!e.isOffensive() && !e.getSkill().isMusic() && e.getSkill().isSelfDispellable() && e.getSkill().getSkillType() != SkillType.TRANSFORMATION
				        && e.getTemplate().getEffectType() != EffectType.Hourglass)
					e.exit();
				else
					return;
	}
}