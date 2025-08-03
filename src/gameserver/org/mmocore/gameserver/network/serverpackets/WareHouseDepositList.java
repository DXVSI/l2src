package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.commons.lang.ArrayUtils;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInfo;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.Warehouse.ItemClassComparator;
import org.mmocore.gameserver.model.items.Warehouse.WarehouseType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALF
 * @data 10.02.2012
 */
public class WareHouseDepositList extends L2GameServerPacket
{
	private int _whtype;
	private long _adena;
	private List<ItemInfo> _itemList;

	public WareHouseDepositList(Player cha, WarehouseType whtype)
	{
		_whtype = whtype.ordinal();
		_adena = cha.getAdena();

		ItemInstance[] items = cha.getInventory().getItems();
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		_itemList = new ArrayList<ItemInfo>(items.length);
		for (ItemInstance item : items)
			if (item.canBeStored(cha, _whtype == 1))
				_itemList.add(new ItemInfo(item));
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x41);
		writeH(_whtype);
		writeQ(_adena);
		writeD(0); // кол занятых слотов
		writeH(0/* size */); // Кол. Валюты в ВХ
		// for (size)
		// writeD(ItemId);
		//
		writeH(_itemList.size());
		for (ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
	}
}