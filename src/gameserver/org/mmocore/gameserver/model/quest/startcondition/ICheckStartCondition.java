package org.mmocore.gameserver.model.quest.startcondition;

import org.mmocore.gameserver.model.Player;

public interface ICheckStartCondition
{
	public boolean checkCondition(Player player);
}
