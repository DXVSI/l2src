package org.mmocore.loginserver.gameservercon.lspackets;

import org.mmocore.loginserver.accounts.Account;
import org.mmocore.loginserver.accounts.SessionManager.Session;
import org.mmocore.loginserver.gameservercon.SendablePacket;

public class PointConnectionLS extends SendablePacket
{
	private String login;
	private int point;

	public PointConnectionLS(Session session)
	{
		Account account = session.getAccount();
		this.login = account.getLogin();
		this.point = account.getPremiumPoint();
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x1d);
		writeS(login);
		writeD(point);
	}
}