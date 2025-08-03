package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.pledge.Clan;
import org.mmocore.gameserver.model.pledge.UnitMember;
import org.mmocore.gameserver.network.serverpackets.PledgeReceiveMemberInfo;

public class RequestPledgeMemberInfo extends L2GameClientPacket
{
	// format: (ch)dS
	@SuppressWarnings("unused")
	private int _pledgeType;
	private String _target;

	@Override
	protected void readImpl()
	{
		_pledgeType = readD();
		_target = readS(16);
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		Clan clan = activeChar.getClan();
		if (clan != null)
		{
			UnitMember cm = clan.getAnyMember(_target);
			if (cm != null)
				activeChar.sendPacket(new PledgeReceiveMemberInfo(cm));
		}
	}
}