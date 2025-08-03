package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.cache.ItemInfoCache;
import org.mmocore.gameserver.model.items.ItemInfo;
import org.mmocore.gameserver.network.serverpackets.ActionFail;
import org.mmocore.gameserver.network.serverpackets.ExRpItemLink;

public class RequestExRqItemLink extends L2GameClientPacket
{
	private int _objectId;

	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}

	@Override
	protected void runImpl()
	{
		ItemInfo item;
		if ((item = ItemInfoCache.getInstance().get(_objectId)) == null)
			sendPacket(ActionFail.STATIC);
		else
			sendPacket(new ExRpItemLink(item));
	}
}