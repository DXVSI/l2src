package org.mmocore.gameserver.network.serverpackets;

import java.util.Collection;

import org.mmocore.gameserver.data.xml.holder.ResidenceHolder;
import org.mmocore.gameserver.model.entity.residence.Castle;
import org.mmocore.gameserver.model.entity.residence.Residence;

public class ExSendManorList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x22);
		Collection<Castle> residences = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		writeD(residences.size());
		for (Residence castle : residences)
		{
			writeD(castle.getId());
			writeS(castle.getName().toLowerCase());
		}
	}
}