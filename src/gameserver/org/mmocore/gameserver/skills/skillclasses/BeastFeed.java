package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.FeedableBeastInstance;
import org.mmocore.gameserver.templates.StatsSet;

public class BeastFeed extends Skill
{
	public BeastFeed(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(final Creature activeChar, GArray<Creature> targets)
	{
		for (final Creature target : targets)
			ThreadPoolManager.getInstance().execute(new RunnableImpl()
			{
				@Override
				public void runImpl() throws Exception
				{
					if (target instanceof FeedableBeastInstance)
						((FeedableBeastInstance) target).onSkillUse((Player) activeChar, _id);
				}
			});
	}
}
