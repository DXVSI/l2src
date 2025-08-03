package org.mmocore.loginserver.gameservercon.lspackets;

import org.mmocore.loginserver.gameservercon.SendablePacket;

public class PingRequest extends SendablePacket
{
	@Override
	protected void writeImpl()
	{
		writeC(0xff);
	}
}