package handler.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInfo;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.network.serverpackets.ExChangeAttributeItemList;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.templates.item.ItemTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 09.06.12
 * Time: 19:31
 */
public class CristallChangeAttr extends ScriptItemHandler
{
	private int[] ITEM_IDS = { 33502, 33835, 33836, 33837, 33833, 33834 };

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;

		Player player = (Player) playable;
		if(player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(SystemMsg.YOU_CAN_NOT_CHANGE_THE_ATTRIBUTE_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}

		switch(item.getItemId())
		{
			case 33502: // Кристалл Изменения Стихий - Оружие (S)
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S, ItemTemplate.Grade.S80);
				break;
			case 33833: // Кристалл Смены Стихии - S	Ивент	u,Позволяет сменить стихию оружия ранга S.
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S);
				break;
			case 33834: // Кристалл Смены Стихии - S80	Ивент	u,Позволяет сменить стихию оружия ранга R.
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S80);
				break;
			case 33835: // Кристалл Смены Стихии - R	Ивент	u,Позволяет сменить стихию оружия ранга R.
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R);
				break;
			case 33836: // Кристалл Смены Стихии - R95	Ивент	u,Позволяет сменить стихию оружия ранга S80.
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R95);
				break;
			case 33837: // Кристалл Смены Стихии - R99	Ивент	u,Позволяет сменить стихию оружия ранга R.
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R99);
				break;
		}
		return true;
	}

	private boolean sendAttributeItemList(int itemId, Player player, ItemTemplate.Grade... grades)
	{
		List<ItemInfo> itemsList = new ArrayList<ItemInfo>();
		ItemInstance[] items = player.getInventory().getItems();
		for(ItemInstance item : items)
			if(item.isWeapon() && item.getAttackElementValue() > 0)
				if(ArrayUtils.contains(grades, item.getCrystalType()))
					itemsList.add(new ItemInfo(item));
		if(itemsList.size() == 0)
		{
			player.sendPacket(SystemMsg.THE_ITEM_FOR_CHANGING_AN_ATTRIBUTE_DOES_NOT_EXIST);
			return false;
		}
		player.sendPacket(new ExChangeAttributeItemList(itemId, itemsList.toArray(new ItemInfo[itemsList.size()])));
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
}
