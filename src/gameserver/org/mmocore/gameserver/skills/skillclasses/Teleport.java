package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.utils.Location;
import org.mmocore.commons.util.Rnd;

public class Teleport extends Skill
{
	private static final Location[] TELEPORT_PLAYER_COORDS = { 
		new Location(219368, 112616, -1330, 63),
		new Location(219176, 119368, -1760, 233),
		new Location(216920, 120264, -1760, 255),
		new Location(210488, 119800, -1345, 97),
		new Location(213688, 116424, -921, 33),
		new Location(215032, 114200, -921, 15),
	};
	
	public Teleport(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for (Creature target : targets)
			if (target != null && target.isPlayer())
			{
				Location coords = TELEPORT_PLAYER_COORDS[Rnd.get(TELEPORT_PLAYER_COORDS.length)];
				target.teleToLocation(coords);
			}
	}
}