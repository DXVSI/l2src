package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;

/**
 * @author ALF
 * @date 21.08.2012
 */
public class RequestCrystallizeItemCancel extends L2GameClientPacket
{

	@Override
	protected void readImpl() throws Exception
	{

	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.sendActionFailed();
	}

}
