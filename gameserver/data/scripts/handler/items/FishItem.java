package handler.items;

import java.util.List;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.reward.RewardData;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.tables.FishTable;
import org.mmocore.gameserver.utils.ItemFunctions;
import org.mmocore.gameserver.utils.Util;

public class FishItem extends ScriptItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		Player player = (Player) playable;

		if(player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
			return false;
		}

		if(!player.getInventory().destroyItem(item, 1L))
		{
			player.sendActionFailed();
			return false;
		}

		int count = 0;
		List<RewardData> rewards = FishTable.getInstance().getFishReward(item.getItemId());
		for(RewardData d : rewards)
		{
			long roll = Util.rollDrop(d.getMinDrop(), d.getMaxDrop(), d.getChance() * Config.RATE_FISH_DROP_COUNT * Config.RATE_DROP_ITEMS * player.getRateItems(), false);
			if(roll > 0)
			{
				ItemFunctions.addItem(player, d.getItemId(), roll, true);
				count++;
			}
		}
		if(count == 0)
			player.sendPacket(SystemMsg.THERE_WAS_NOTHING_FOUND_INSIDE);
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return FishTable.getInstance().getFishIds();
	}
}