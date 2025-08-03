package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.commission.CommissionShopManager;
import org.mmocore.gameserver.model.Player;

/**
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатия вкладки "Регистрация", запращивает список вещей, которые можно положить в коммиссионный магазин Отправляет
 *         {@link org.mmocore.gameserver.network.serverpackets.ExResponseCommissionItemList}
 */
public class RequestCommissionRegistrableItemList extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		// Do nothing
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().showRegistrableItems(player);
	}
}
