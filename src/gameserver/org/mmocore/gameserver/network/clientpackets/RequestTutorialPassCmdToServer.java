package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.QuestManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.quest.Quest;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket
{
	// format: cS

	String _bypass = null;

	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Quest tutorial = QuestManager.getQuest(255);

		if (tutorial != null)
			player.processQuestEvent(tutorial.getName(), _bypass, null);
	}
}