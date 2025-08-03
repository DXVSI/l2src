package org.mmocore.loginserver.gameservercon.gspackets;

import org.mmocore.loginserver.accounts.Account;
import org.mmocore.loginserver.gameservercon.ReceivablePacket;

public class PointConnectionLS extends ReceivablePacket
{
	private String accounts;
	private int point;

	@Override
	protected void readImpl()
	{
		accounts = readS();
		point = readD();
	}

	@Override
	protected void runImpl()
	{
		Account acc = new Account(accounts);
		acc.restore();
		acc.setPremiumPoint(point);
		acc.update();
	}
}