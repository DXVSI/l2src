package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.GameClient.GameClientState;
import org.mmocore.gameserver.network.serverpackets.ActionFail;
import org.mmocore.gameserver.network.serverpackets.CharacterSelectionInfo;
import org.mmocore.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;
import org.mmocore.gameserver.network.serverpackets.RestartResponse;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;

public class RequestRestart extends L2GameClientPacket
{
	/**
	 * packet type id 0x57 format: c
	 */

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

		if (activeChar.isInObserverMode())
		{
			activeChar.sendPacket(Msg.OBSERVERS_CANNOT_PARTICIPATE, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}

		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RESTART_WHILE_IN_COMBAT, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}

		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}

		if (activeChar.isBlocked() && !activeChar.isFlying()) // Разрешаем
		                                                      // выходить из
		                                                      // игры если
		                                                      // используется
		                                                      // сервис
		                                                      // HireWyvern.
		                                                      // Вернет в
		                                                      // начальную
		                                                      // точку.
		{
			activeChar.sendMessage(new CustomMessage("org.mmocore.gameserver.clientpackets.RequestRestart.OutOfControl", activeChar));
			activeChar.sendPacket(RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}

		if (getClient() != null)
			getClient().setState(GameClientState.AUTHED);

		ccpGuard.Protection.doDisconection(getClient());

		activeChar.restart();
		// send char list
		CharacterSelectionInfo cl = new CharacterSelectionInfo(getClient().getLogin(), getClient().getSessionKey().playOkID1);
		ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(cl.getCharInfo());
		sendPacket(RestartResponse.OK, cl, vl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}