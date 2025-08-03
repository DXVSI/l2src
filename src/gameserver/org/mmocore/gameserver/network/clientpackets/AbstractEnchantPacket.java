package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.items.etcitems.EnchantScrollInfo;
import org.mmocore.gameserver.model.items.etcitems.EnchantScrollTarget;

/**
 * @author ALF
 * @date 16.10.2012
 */
public abstract class AbstractEnchantPacket extends L2GameClientPacket
{
	protected boolean isValidPlayer(Player player)
	{
		if (player.isActionsDisabled())
			return false;

		if (player.isInTrade())
			return false;

		if (player.isInStoreMode())
			return false;

		return true;
	}

	protected boolean checkItem(ItemInstance item, EnchantScrollInfo esi)
	{
		if (item.isStackable())
			return false;

		if (esi.getTargetItems() != null)
			if (esi.getTargetItems().contains(item.getItemId()))
				return true;

		if (item.getTemplate().getItemGrade().externalOrdinal != esi.getGrade().externalOrdinal)
			return false;

		if (item.isArmor() && esi.getTarget() != EnchantScrollTarget.ARMOR)
			return false;

		if (item.isWeapon() && esi.getTarget() != EnchantScrollTarget.WEAPON)
			return false;

		if (!item.canBeEnchanted())
			return false;

		if (item.getLocation() != ItemInstance.ItemLocation.INVENTORY && item.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
			return false;

		return true;
	}
}
