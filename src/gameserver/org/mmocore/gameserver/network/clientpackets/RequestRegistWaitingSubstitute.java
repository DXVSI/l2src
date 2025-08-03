package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.party.PartySubstitute;

/**
 * @author ALF
 * @date 22.08.2012
 */
public final class RequestRegistWaitingSubstitute extends L2GameClientPacket
{
	private int _key;

	@Override
	protected void readImpl()
	{
		_key = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if (activeChar == null)
			return;

		switch (_key)
		{
			case 0:
				PartySubstitute.getInstance().removePlayerFromParty(activeChar);
				break;
			case 1:
				PartySubstitute.getInstance().addPlayerToParty(activeChar);
				break;
			default:
				System.out.println("RequestRegistWaitingSubstitute: key is " + _key);
				break;
		}
	}
}
