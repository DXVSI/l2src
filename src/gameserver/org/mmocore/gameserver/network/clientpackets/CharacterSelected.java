package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.GameClient;
import org.mmocore.gameserver.network.GameClient.GameClientState;
import org.mmocore.gameserver.network.serverpackets.ActionFail;
import org.mmocore.gameserver.network.serverpackets.CharSelected;
import org.mmocore.gameserver.utils.AutoBan;

public class CharacterSelected extends L2GameClientPacket
{
	private int _charSlot;

	/**
	 * Format: cdhddd
	 */
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
	}

	@Override
	protected void runImpl()
	{
		GameClient client = getClient();

		if (client.getActiveChar() != null)
			return;

		if (Config.SECOND_AUTH_ENABLED && !client.getSecondaryAuth().isAuthed())
		{
			client.getSecondaryAuth().openDialog();
			return;
		}

		int objId = client.getObjectIdForSlot(_charSlot);
		if (AutoBan.isBanned(objId))
		{
			sendPacket(ActionFail.STATIC);
			return;
		}

		Player activeChar = client.loadCharFromDisk(_charSlot);
		if (activeChar == null)
		{
			sendPacket(ActionFail.STATIC);
			return;
		}

		if (activeChar.getAccessLevel() < 0)
			activeChar.setAccessLevel(0);

		if (!ccpGuard.Protection.checkPlayerWithHWID(client, activeChar.getObjectId(), activeChar.getName()))
			return;

		client.setState(GameClientState.IN_GAME);

		sendPacket(new CharSelected(activeChar, client.getSessionKey().playOkID1));
	}
}