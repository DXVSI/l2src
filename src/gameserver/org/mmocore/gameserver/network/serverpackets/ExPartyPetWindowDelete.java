package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.Summon;

public class ExPartyPetWindowDelete extends L2GameServerPacket
{
	private int _summonObjectId;
	private int _ownerObjectId;
	private String _summonName;

	public ExPartyPetWindowDelete(Summon summon)
	{
		_summonObjectId = summon.getObjectId();
		_summonName = summon.getName();
		_ownerObjectId = summon.getPlayer().getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x6B);
		writeD(_summonObjectId);
		writeD(_ownerObjectId);
		writeS(_summonName);
	}
}