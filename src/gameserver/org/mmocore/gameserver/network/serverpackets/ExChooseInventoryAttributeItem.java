package org.mmocore.gameserver.network.serverpackets;

import gnu.trove.list.array.TIntArrayList;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.etcitems.AttributeStoneInfo;
import org.mmocore.gameserver.model.items.etcitems.AttributeStoneManager;
import org.mmocore.gameserver.templates.item.ItemTemplate;

public class ExChooseInventoryAttributeItem extends L2GameServerPacket
{
	private TIntArrayList _attributableItems;
	private int _itemId;
	private int _stoneLvl;
	private int[] _att;

	public ExChooseInventoryAttributeItem(Player player, ItemInstance item)
	{
		boolean Bound = false;
		if (item.getItemId() >= 34649 && item.getItemId() <= 34654)
			Bound = true;
		_attributableItems = new TIntArrayList();
		ItemInstance[] items = player.getInventory().getItems();
		for (ItemInstance _item : items)
			if (_item.getCrystalType() != ItemTemplate.Grade.NONE && (_item.isArmor() || _item.isWeapon()))
			{
				if (Bound && _item.getTemplate().isBound())
					_attributableItems.add(_item.getObjectId());
				else if (!Bound)
					_attributableItems.add(_item.getObjectId());
			}

		_itemId = item.getItemId();
		_att = new int[6];

		AttributeStoneInfo asi = AttributeStoneManager.getStoneInfo(_itemId);
		_att[asi.getElement().getId()] = 1;
		_stoneLvl = asi.getStoneLevel();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x63);
		writeD(_itemId);
		for (int i : _att)
			writeD(i);
		writeD(_stoneLvl); // max enchant lvl
		writeD(_attributableItems.size()); // equipable items count
		for (int itemObjId : _attributableItems.toArray())
			writeD(itemObjId); // itemObjId
	}
}