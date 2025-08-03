package org.mmocore.gameserver.network.clientpackets;

public class ReplyGameGuardQuery extends L2GameClientPacket
{
	// Format: cdddd
	public byte[] _data = new byte[80];

	@Override
	protected void readImpl()
	{
		ccpGuard.Protection.doReadReplyGameGuard(getClient(), _buf, _data);
	}

	@Override
	protected void runImpl()
	{
		ccpGuard.Protection.doReplyGameGuard(getClient(), _data);
	}
}