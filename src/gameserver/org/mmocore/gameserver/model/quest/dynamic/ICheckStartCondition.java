package org.mmocore.gameserver.model.quest.dynamic;

import org.mmocore.gameserver.model.Player;

public interface ICheckStartCondition
{
	public boolean checkCondition(Player player);
}