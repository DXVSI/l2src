package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.network.serverpackets.NewCharacterSuccess;

public class NewCharacter extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		sendPacket(new NewCharacterSuccess());
	}
}