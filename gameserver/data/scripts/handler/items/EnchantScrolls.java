package handler.items;

import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.etcitems.EnchantScrollManager;
import org.mmocore.gameserver.network.serverpackets.ChooseInventoryItem;

public class EnchantScrolls extends ScriptItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		Player player = (Player) playable;

		if(player.getEnchantScroll() != null)
			return false;

		player.setEnchantScroll(item);
		player.sendPacket(new ChooseInventoryItem(item.getItemId()));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return EnchantScrollManager.getEnchantScrollIds();
	}
}