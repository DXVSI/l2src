package org.mmocore.loginserver.gameservercon.gspackets;

import org.mmocore.loginserver.accounts.Account;
import org.mmocore.loginserver.gameservercon.ReceivablePacket;
import org.mmocore.loginserver.gameservercon.lspackets.IsLocIp;

/**
 * @author ALF
 * @date 31.07.2012
 */
public class RequestIsLocIp extends ReceivablePacket
{
	private int _objId;
	private String _login;

	@Override
	protected void readImpl()
	{
		_objId = readD();
		_login = readS();
	}

	@Override
	protected void runImpl()
	{
		Account ac = new Account(_login);
		ac.restore();
		String _ip = ac.getAllowedIP();
		getGameServer().sendPacket(new IsLocIp(_objId, _ip));
	}

}
