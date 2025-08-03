package org.mmocore.gameserver.model.quest.startcondition.impl;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.quest.startcondition.ICheckStartCondition;

public final class QuestCompletedCondition implements ICheckStartCondition
{
	private final String questName;

	public QuestCompletedCondition(String questName)
	{
		this.questName = questName;
	}

	@Override
	public final boolean checkCondition(Player player)
	{
		return player.isQuestCompleted(questName);
	}
}
