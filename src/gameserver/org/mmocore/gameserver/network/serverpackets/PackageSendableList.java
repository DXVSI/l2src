package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.commons.lang.ArrayUtils;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInfo;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date 20:46/16.05.2011
 */
public class PackageSendableList extends L2GameServerPacket
{
	private int _targetObjectId;
	private long _adena;
	private List<ItemInfo> _itemList;

	public PackageSendableList(int objectId, Player cha)
	{
		_adena = cha.getAdena();
		_targetObjectId = objectId;

		ItemInstance[] items = cha.getInventory().getItems();
		ArrayUtils.eqSort(items, Warehouse.ItemClassComparator.getInstance());
		_itemList = new ArrayList<ItemInfo>(items.length);
		for (ItemInstance item : items)
			if (item.getTemplate().isFreightable())
				_itemList.add(new ItemInfo(item));
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xD2);
		writeD(_targetObjectId);
		writeQ(_adena);
		writeD(_itemList.size());
		for (ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
	}
}
