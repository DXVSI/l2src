package org.mmocore.gameserver.loginservercon.lspackets;

import org.mmocore.gameserver.loginservercon.LoginServerCommunication;
import org.mmocore.gameserver.loginservercon.ReceivablePacket;
import org.mmocore.gameserver.network.GameClient;

public class PointConnectionGS extends ReceivablePacket
{

	private int point;
	private String acc;

	@Override
	protected void readImpl()
	{
		acc = readS();
		point = readD();
	}

	@Override
	protected void runImpl()
	{
		GameClient client = LoginServerCommunication.getInstance().getAuthedClient(acc);
		if (client != null)
			client.setPremiumPoint(point);
	}
}