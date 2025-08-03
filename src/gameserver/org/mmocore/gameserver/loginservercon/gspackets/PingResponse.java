package org.mmocore.gameserver.loginservercon.gspackets;

import org.mmocore.gameserver.loginservercon.SendablePacket;

public class PingResponse extends SendablePacket
{
	@Override
	protected void writeImpl()
	{
		writeC(0xff);
		writeQ(System.currentTimeMillis());
	}
}