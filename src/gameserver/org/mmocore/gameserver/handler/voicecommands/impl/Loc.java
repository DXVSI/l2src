package org.mmocore.gameserver.handler.voicecommands.impl;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.handler.voicecommands.IVoicedCommandHandler;
import org.mmocore.gameserver.loginservercon.LoginServerCommunication;
import org.mmocore.gameserver.loginservercon.SendablePacket;
import org.mmocore.gameserver.loginservercon.gspackets.RequestIsLocIp;
import org.mmocore.gameserver.loginservercon.gspackets.RequestLocIp;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.ThreadPoolManager;

public class Loc implements IVoicedCommandHandler
{
	private static final String[] commandList = { "loc" };

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		int _objId = activeChar.getObjectId();
		String _login = activeChar.getAccountName();
		String _ip = activeChar.getIP();
		boolean _loc = false;

		if (command.equals("loc"))
		{
			if (target != null)
			{
				if (target.equalsIgnoreCase("on"))
					_loc = true;

				SendablePacket sp = new RequestLocIp(_login, _ip, _loc);
				LoginServerCommunication.getInstance().sendPacket(sp);
				ThreadPoolManager.getInstance().schedule(new CheckLocStatuc(_objId, _login), 1000);
			}
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return commandList;
	}

	class CheckLocStatuc extends RunnableImpl
	{
		private int _objId;
		private String _login;

		public CheckLocStatuc(int objId, String login)
		{
			_objId = objId;
			_login = login;
		}

		@Override
		public void runImpl() throws Exception
		{
			SendablePacket sp = new RequestIsLocIp(_objId, _login);
			LoginServerCommunication.getInstance().sendPacket(sp);
		}

	}
}
