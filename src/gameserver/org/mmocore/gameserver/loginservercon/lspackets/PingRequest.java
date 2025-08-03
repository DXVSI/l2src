package org.mmocore.gameserver.loginservercon.lspackets;

import org.mmocore.gameserver.loginservercon.LoginServerCommunication;
import org.mmocore.gameserver.loginservercon.ReceivablePacket;
import org.mmocore.gameserver.loginservercon.gspackets.PingResponse;

public class PingRequest extends ReceivablePacket
{
	@Override
	public void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{
		LoginServerCommunication.getInstance().sendPacket(new PingResponse());
	}
}