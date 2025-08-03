package org.mmocore.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;
import org.mmocore.commons.data.xml.AbstractFileParser;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.data.xml.holder.RecipeHolder;
import org.mmocore.gameserver.model.items.etcitems.Recipe;
import org.mmocore.gameserver.model.items.etcitems.RecipeIngredient;
import org.mmocore.gameserver.model.items.etcitems.RecipeProduction;

public final class RecipeParser extends AbstractFileParser<RecipeHolder>
{
	private static final RecipeParser _instance = new RecipeParser();

	public static RecipeParser getInstance()
	{
		return _instance;
	}

	private RecipeParser()
	{
		super(RecipeHolder.getInstance());
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/recipes.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "recipes.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			int id = Integer.parseInt(element.attributeValue("id"));
			int recipeId = Integer.parseInt(element.attributeValue("recipeId"));
			int mpConsume = Integer.parseInt(element.attributeValue("mpConsume"));
			int craftLevel = Integer.parseInt(element.attributeValue("craftLevel"));
			boolean isCommon = element.attributeValue("type").equalsIgnoreCase("common");
			int successRate = Integer.parseInt(element.attributeValue("successRate"));
			
			Recipe recipe = new Recipe(id, recipeId, mpConsume, craftLevel, isCommon, successRate);
			
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();

				if ("ingredient".equalsIgnoreCase(subElement.getName()))
				{
					int itemId = Integer.parseInt(subElement.attributeValue("id"));
					int count = Integer.parseInt(subElement.attributeValue("count"));
					
					recipe.addIngradient(new RecipeIngredient(itemId, count));
				}
				else if ("production".equalsIgnoreCase(subElement.getName()))
				{
					int itemId = Integer.parseInt(subElement.attributeValue("id"));
					int count = Integer.parseInt(subElement.attributeValue("count"));
					int chance = Integer.parseInt(subElement.attributeValue("chance"));
					
					recipe.addProduction(new RecipeProduction(itemId, count, chance));
				}
			}
			getHolder().addRecipe(recipe);
		}
	}
}
