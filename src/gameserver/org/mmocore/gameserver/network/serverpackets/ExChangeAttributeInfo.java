package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.base.Element;
import org.mmocore.gameserver.model.items.ItemInstance;

public class ExChangeAttributeInfo extends L2GameServerPacket
{
	private int _crystalItemId;
	private int _attributes;
	private int _itemObjId;

	public ExChangeAttributeInfo(int crystalItemId, ItemInstance item)
	{
		_crystalItemId = crystalItemId;
		_attributes = 0;
		for (Element e : Element.VALUES)
		{
			if (e == item.getAttackElement())
				continue;
			_attributes |= e.getMask();
		}
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x119);
		writeD(_crystalItemId);// unk??
		writeD(_attributes);
		writeD(_itemObjId);// unk??
	}
}