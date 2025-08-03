package org.mmocore.gameserver.network.serverpackets;

public class ExBR_RecentProductListPacket extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xDD);
		// TODO dx[dhddddcccccdd]
	}
}