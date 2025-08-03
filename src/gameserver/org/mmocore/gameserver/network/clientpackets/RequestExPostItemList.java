package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ExReplyPostItemList;
import org.mmocore.gameserver.network.serverpackets.ExShowReceivedPostList;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;

/**
 * Нажатие на кнопку "send mail" в списке из {@link ExShowReceivedPostList}, запрос создания нового письма В ответ шлется {@link ExReplyPostItemList}
 */
public class RequestExPostItemList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// just a trigger
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
			activeChar.sendActionFailed();

		if (!Config.ALLOW_MAIL)
		{
			activeChar.sendMessage(new CustomMessage("mail.Disabled", activeChar));
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new ExReplyPostItemList(activeChar));
	}
}