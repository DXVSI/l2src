package org.mmocore.gameserver.network.serverpackets;

public class ExResponseFreeServer extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x78);
		// just trigger
	}
}