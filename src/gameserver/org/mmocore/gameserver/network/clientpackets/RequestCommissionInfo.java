package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.commission.CommissionShopManager;
import org.mmocore.gameserver.model.Player;

/**
 * /**
 * 
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии на итем в окне регистрации вещей на продажу. Отправляет
 *         {@link org.mmocore.gameserver.network.serverpackets.ExResponseCommissionInfo}
 */
public class RequestCommissionInfo extends L2GameClientPacket
{
	private int itemObjId;

	@Override
	protected void readImpl() throws Exception
	{
		itemObjId = readD(); // id выбранного итема
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().showCommissionInfo(player, itemObjId);
	}
}
