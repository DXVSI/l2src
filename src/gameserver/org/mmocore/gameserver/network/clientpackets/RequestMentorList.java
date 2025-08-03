package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ExMentorList;

/**
 * Приходит при нажатии кнопки Friends в клиенте. Не имеет структуры, ответом на этот запрос является пакет
 * {@link org.mmocore.gameserver.network.serverpackets.ExMentorList}
 */
public class RequestMentorList extends L2GameClientPacket {

	@Override
	protected void runImpl() {
		// triggger
	}

	@Override
	protected void readImpl() {
		Player activeChar = getClient().getActiveChar();
		sendPacket(new ExMentorList(activeChar));
	}
}
