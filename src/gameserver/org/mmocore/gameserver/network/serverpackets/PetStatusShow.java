package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.Summon;

public class PetStatusShow extends L2GameServerPacket
{
	private int _summonType;
	private int _summonId;

	public PetStatusShow(Summon summon)
	{
		_summonType = summon.getSummonType();
		_summonId = summon.getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xb1);
		writeD(_summonType);
		writeD(_summonId);// L2WT GOD
	}
}