package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class RequestDeletePartySubstitute extends L2GameClientPacket
{

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();

		if (player == null)
			return;

		System.out.println("RequestDeletePartySubstitute");
	}
}
