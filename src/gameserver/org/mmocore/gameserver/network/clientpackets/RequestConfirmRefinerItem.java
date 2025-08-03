package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.etcitems.LifeStoneInfo;
import org.mmocore.gameserver.model.items.etcitems.LifeStoneManager;
import org.mmocore.gameserver.network.serverpackets.ExPutIntensiveResultForVariationMake;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.templates.item.ItemTemplate.Grade;

public class RequestConfirmRefinerItem extends AbstractRefinePacket
{
	// format: (ch)dd
	private int _targetItemObjId;
	private int _refinerItemObjId;

	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);

		if (targetItem == null || refinerItem == null)
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		LifeStoneInfo lsi = LifeStoneManager.getStoneInfo(refinerItem.getItemId());

		if (lsi == null)
			return;

		if (!isValid(activeChar, targetItem, refinerItem))
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		final int refinerItemId = refinerItem.getItemId();
		final Grade grade = targetItem.getTemplate().getItemGrade();
		final int gemStoneId = getGemStoneId(grade);
		final int gemStoneCount = getGemStoneCount(lsi.getGrade(), grade);

		SystemMessage sm = new SystemMessage(SystemMessage.REQUIRES_S1_S2).addNumber(gemStoneCount).addItemName(gemStoneId);
		activeChar.sendPacket(new ExPutIntensiveResultForVariationMake(_refinerItemObjId, refinerItemId, gemStoneId, gemStoneCount), sm);
	}
}