package org.mmocore.gameserver.instancemanager.commission;

import org.mmocore.commons.dao.JdbcEntityState;
import org.mmocore.gameserver.model.items.ItemContainer;
import org.mmocore.gameserver.model.items.ItemInstance;

import java.util.Collection;

/**
 * @author : Darvin
 */
public class CommissionItemContainer extends ItemContainer
{

	public ItemInstance.ItemLocation getItemLocation()
	{
		return ItemInstance.ItemLocation.COMMISSION;
	}

	@Override
	protected void onAddItem(ItemInstance item)
	{
		item.setLocation(getItemLocation());
		item.setLocData(0);
		if (item.getJdbcState().isSavable())
		{
			item.save();
		}
		else
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}
	}

	@Override
	protected void onModifyItem(ItemInstance item)
	{
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.update();
	}

	@Override
	protected void onRemoveItem(ItemInstance item)
	{
		item.setLocData(-1);
	}

	@Override
	protected void onDestroyItem(ItemInstance item)
	{
		item.setCount(0L);
		item.delete();
	}

	public void restore()
	{
		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByLoc(getItemLocation());
			for (ItemInstance item : items)
				_items.add(item);
		}
		finally
		{
			writeUnlock();
		}
	}
}
