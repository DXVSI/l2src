package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.DimensionalRift;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.party.Party;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;

public class RequestWithDrawalParty extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		Party party = activeChar.getParty();
		if (party == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("Вы не можете сейчас выйти из группы."); // TODO
			                                                                // [G1ta0]
			                                                                // custom
			                                                                // message
			return;
		}

		Reflection r = activeChar.getParty().getReflection();
		if (r != null && r instanceof DimensionalRift && activeChar.getReflection().equals(r))
			activeChar.sendMessage(new CustomMessage("l2r.gameserver.network.clientpackets.RequestWithDrawalParty.Rift", activeChar));
		else if (r != null && activeChar.isInCombat())
			activeChar.sendMessage("Вы не можете сейчас выйти из группы.");
		else
			activeChar.leaveParty();
	}
}