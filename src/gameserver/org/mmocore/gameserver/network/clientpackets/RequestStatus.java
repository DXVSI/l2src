package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.network.serverpackets.SendStatus;

public final class RequestStatus extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		getClient().close(new SendStatus());
	}
}