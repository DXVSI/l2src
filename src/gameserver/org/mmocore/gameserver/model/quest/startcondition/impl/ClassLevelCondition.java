package org.mmocore.gameserver.model.quest.startcondition.impl;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.quest.startcondition.ICheckStartCondition;
import org.apache.commons.lang3.ArrayUtils;

public class ClassLevelCondition implements ICheckStartCondition
{
	private int[] classLevels;

	public ClassLevelCondition(int... classLevels)
	{
		this.classLevels = classLevels;
	}

	@Override
	public boolean checkCondition(Player player)
	{
		return ArrayUtils.contains(classLevels, player.getClassId().getClassLevel().ordinal());
	}
}
