package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.DimensionalRift;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.party.Party;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;

public class RequestOustPartyMember extends L2GameClientPacket
{
	// Format: cS
	private String _name;

	@Override
	protected void readImpl()
	{
		_name = readS(16);
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		Party party = activeChar.getParty();
		if (party == null || !activeChar.getParty().isLeader(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("Вы не можете сейчас выйти из группы.");// TODO
			                                                               // [G1ta0]
			                                                               // custom
			                                                               // message
			return;
		}

		Player member = party.getPlayerByName(_name);

		if (member == activeChar)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (member == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		Reflection r = party.getReflection();

		if (r != null && r instanceof DimensionalRift && member.getReflection().equals(r))
			activeChar.sendMessage(new CustomMessage("l2r.gameserver.network.clientpackets.RequestOustPartyMember.CantOustInRift", activeChar));
		else if (r != null && !(r instanceof DimensionalRift))
			activeChar.sendMessage(new CustomMessage("l2r.gameserver.network.clientpackets.RequestOustPartyMember.CantOustInDungeon", activeChar));
		else
			party.removePartyMember(member, true);
	}
}