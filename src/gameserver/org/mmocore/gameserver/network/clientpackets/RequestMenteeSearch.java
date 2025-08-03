package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ExMenteeSearch;

/**
 * User: Andrey A. Date: 11.11.12 Time: 23:48
 */
public class RequestMenteeSearch extends L2GameClientPacket {
	private int maxLevel;
	private int minLevel;
	private int page;

	@Override
	protected void readImpl() throws Exception {
		page = readD();
		minLevel = readD();
		maxLevel = readD();
	}

	@Override
	protected void runImpl() throws Exception {
		Player activeChar = getClient().getActiveChar();
		activeChar.sendPacket(new ExMenteeSearch(activeChar, page, minLevel, maxLevel));
	}
}
