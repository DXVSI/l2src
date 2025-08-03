package org.mmocore.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Collection;

import org.mmocore.commons.data.xml.AbstractHolder;
import org.mmocore.gameserver.model.items.etcitems.Recipe;

public final class RecipeHolder extends AbstractHolder
{
	private static final RecipeHolder _instance = new RecipeHolder();

	private final TIntObjectHashMap<Recipe> _listByRecipeId = new TIntObjectHashMap<>();
	private final TIntObjectHashMap<Recipe> _listByRecipeItem = new TIntObjectHashMap<>();

	public static RecipeHolder getInstance()
	{
		return _instance;
	}

	public void addRecipe(Recipe recipe)
	{
		_listByRecipeId.put(recipe.getId(), recipe);
		_listByRecipeItem.put(recipe.getRecipeId(), recipe);
	}

	public Recipe getRecipeById(int id)
	{
		return _listByRecipeId.get(id);
	}

	public Recipe getRecipeByRecipeItem(int id)
	{
		return _listByRecipeItem.get(id);
	}

	public Collection<Recipe> getRecipes()
	{
		return _listByRecipeId.valueCollection();
	}

	@Override
	public int size()
	{
		return _listByRecipeId.size();
	}

	@Override
	public void clear()
	{
		_listByRecipeId.clear();
		_listByRecipeItem.clear();
	}
}
