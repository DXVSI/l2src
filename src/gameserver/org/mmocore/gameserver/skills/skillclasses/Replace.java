package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Summon;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.utils.Location;

public class Replace extends Skill
{

	public Replace(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if (!(activeChar instanceof Player))
			return;
		final Player activePlayer = activeChar.getPlayer();

		// TODO: Уточнить как долно работать...
		for (Summon activePet : activeChar.getPets())
		{
			if (!(activePet instanceof Summon))
				return; // TODO: SysMessage

			Location loc_pet = activePet.getLoc();
			Location loc_cha = activePlayer.getLoc();
			activePlayer.teleToLocation(loc_pet);
			activePet.teleToLocation(loc_cha);
		}
	}
}
