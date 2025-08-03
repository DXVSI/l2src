package org.mmocore.gameserver.data.xml;

import org.mmocore.gameserver.data.StringHolder;
import org.mmocore.gameserver.data.htm.HtmCache;
import org.mmocore.gameserver.data.xml.holder.BuyListHolder;
import org.mmocore.gameserver.data.xml.holder.MultiSellHolder;
import org.mmocore.gameserver.data.xml.holder.ProductHolder;
import org.mmocore.gameserver.data.xml.holder.RecipeHolder;
import org.mmocore.gameserver.data.xml.parser.*;
import org.mmocore.gameserver.instancemanager.ReflectionManager;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.tables.SpawnTable;

/**
 * @author VISTALL
 * @date 20:55/30.11.2010
 */
public abstract class Parsers
{
	public static void parseAll()
	{
		HtmCache.getInstance().reload();
		StringHolder.getInstance().load();

		//
		SkillTable.getInstance().load(); // - SkillParser.getInstance();
		OptionDataParser.getInstance().load();
		EtcItemParser.getInstance().load();
		WeaponItemParser.getInstance().load();
		ArmorItemParser.getInstance().load();
		//
		NpcParser.getInstance().load();

		DomainParser.getInstance().load();
		RestartPointParser.getInstance().load();

		StaticObjectParser.getInstance().load();
		DoorParser.getInstance().load();
		ZoneParser.getInstance().load();
		SpawnTable.getInstance();
		SpawnParser.getInstance().load();
		InstantZoneParser.getInstance().load();
		WalkerRoutesParser.getInstance().load();

		RecipeParser.getInstance().load();

		ReflectionManager.getInstance();
		//
		AirshipDockParser.getInstance().load();
		SkillAcquireParser.getInstance().load();
		//
		ResidenceParser.getInstance().load();
		ShuttleTemplateParser.getInstance().load();
		EventParser.getInstance().load();
		// support(cubic & agathion)
		CubicParser.getInstance().load();
		//
		BuyListHolder.getInstance();
		RecipeHolder.getInstance();
		MultiSellHolder.getInstance();
		ProductHolder.getInstance();
		// AgathionParser.getInstance();
		// item support
		HennaParser.getInstance().load();
		JumpTracksParser.getInstance().load();
		SoulCrystalParser.getInstance().load();
		ArmorSetsParser.getInstance().load();
		FishDataParser.getInstance().load();

		// etc
		PetitionGroupParser.getInstance().load();
		// Player templates
		PlayerTemplateParser.getInstance().load();
		// Classes load
		ClassDataParser.getInstance().load();
		// LvL bonus load
		LevelBonusParser.getInstance().load();

		SummonPointsParser.getInstance().load();

		StatuesSpawnParser.getInstance().load();

	}
}
