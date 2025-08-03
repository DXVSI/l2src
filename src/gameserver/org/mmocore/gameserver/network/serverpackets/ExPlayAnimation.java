package org.mmocore.gameserver.network.serverpackets;

public class ExPlayAnimation extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x5B);
		// TODO dcdS
	}
}