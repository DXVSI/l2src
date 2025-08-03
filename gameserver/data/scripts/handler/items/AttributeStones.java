package handler.items;

import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.etcitems.AttributeStoneManager;
import org.mmocore.gameserver.network.serverpackets.ExChooseInventoryAttributeItem;

/**
 * @author SYS
 * @rework ALF
 * @data 26.06.2012
 */
public class AttributeStones extends ScriptItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		Player player = (Player) playable;

		if(player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(Msg.YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}

		if(player.getEnchantScroll() != null)
			return false;

		player.setEnchantScroll(item);
		player.sendPacket(Msg.PLEASE_SELECT_ITEM_TO_ADD_ELEMENTAL_POWER);
		player.sendPacket(new ExChooseInventoryAttributeItem(player, item));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return AttributeStoneManager.getAttributeStoneIds();
	}
}