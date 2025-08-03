package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.commission.CommissionShopManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author KilRoy
 */
public class RequestCommissionDelete extends L2GameClientPacket
{
	public long _bidId;
	public int _unk2;
	public int _unk3;

	@Override
	protected void readImpl()
	{
		_bidId = readQ();
		_unk2 = readD();
		_unk3 = readD();

	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		CommissionShopManager.getInstance().removeExpiredItemsBid(activeChar, _bidId);
		CommissionShopManager.getInstance().showPlayerRegisteredItems(activeChar);
		CommissionShopManager.getInstance().showRegistrableItems(activeChar);
		activeChar.sendPacket(SystemMsg.CANCELLATION_OF_SALE_FOR_THE_ITEM_IS_SUCCESSFUL);
	}
}