package org.mmocore.gameserver.network.serverpackets;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class ExWaitWaitingSubStituteInfo extends L2GameServerPacket
{
	public static final int WAITING_CANCEL = 0;
	public static final int WAITING_OK = 1;

	private int _code;

	public ExWaitWaitingSubStituteInfo(int code)
	{
		_code = code;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x104);
		writeD(_code);
	}

}
