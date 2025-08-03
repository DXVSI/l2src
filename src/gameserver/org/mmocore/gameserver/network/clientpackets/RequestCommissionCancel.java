package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.commission.CommissionShopManager;
import org.mmocore.gameserver.model.Player;

/**
 * @author KilRoy
 */
public class RequestCommissionCancel extends L2GameClientPacket
{

	@Override
	protected void readImpl()
	{
		//
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		CommissionShopManager.getInstance().showPlayerRegisteredItems(activeChar);
		CommissionShopManager.getInstance().showRegistrableItems(activeChar);
	}

}
