package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.GameObjectsStorage;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.party.Party;
import org.mmocore.gameserver.model.party.PartySubstitute;
import org.mmocore.gameserver.network.serverpackets.ExRegistPartySubstitute;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class RequestRegistPartySubstitute extends L2GameClientPacket
{
	private int _changeCharId;

	@Override
	protected void readImpl()
	{
		_changeCharId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();

		if (player == null)
			return;

		Party p = player.getParty();

		if (p == null)
			return;

		if (!p.isLeader(player))
			return;

		Player chp = GameObjectsStorage.getPlayer(_changeCharId);

		if (!p.containsMember(chp))
			return;

		PartySubstitute.getInstance().addPlayerToReplace(chp);

		player.sendPacket(new ExRegistPartySubstitute(_changeCharId, ExRegistPartySubstitute.REGISTER_OK));

	}
}
