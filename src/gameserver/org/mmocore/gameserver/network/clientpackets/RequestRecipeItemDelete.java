package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.dao.CharacterRecipebookDAO;
import org.mmocore.gameserver.data.xml.holder.RecipeHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.items.etcitems.Recipe;
import org.mmocore.gameserver.network.serverpackets.RecipeBookItemList;

public class RequestRecipeItemDelete extends L2GameClientPacket
{
	private int _recipeId;

	@Override
	protected void readImpl()
	{
		_recipeId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
		{
			activeChar.sendActionFailed();
			return;
		}

		Recipe rp = RecipeHolder.getInstance().getRecipeById(_recipeId);
		if (rp == null || !activeChar.findRecipe(_recipeId))
		{
			activeChar.sendActionFailed();
			return;
		}

		CharacterRecipebookDAO.getInstance().unregisterRecipe(activeChar.getObjectId(), _recipeId);
		if (activeChar.getDwarvenRecipeBook().contains(rp))
		{
			activeChar.removeDwarvenRecipe(_recipeId);
		}
		else
		{
			activeChar.removeCommonRecipe(_recipeId);
		}

		activeChar.sendPacket(new RecipeBookItemList(activeChar, !rp.isCommon()));
	}
}