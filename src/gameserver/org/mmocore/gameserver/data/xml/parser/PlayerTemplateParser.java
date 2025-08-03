package org.mmocore.gameserver.data.xml.parser;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.mmocore.commons.data.xml.AbstractDirParser;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.data.xml.holder.PlayerTemplateHolder;
import org.mmocore.gameserver.model.base.ClassType;
import org.mmocore.gameserver.model.base.Race;
import org.mmocore.gameserver.model.base.Sex;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.templates.item.StartItem;
import org.mmocore.gameserver.templates.player.BaseArmorDefence;
import org.mmocore.gameserver.templates.player.BaseJewelDefence;
import org.mmocore.gameserver.templates.player.LvlUpData;
import org.mmocore.gameserver.templates.player.PlayerTemplate;
import org.mmocore.gameserver.templates.player.StatAttributes;
import org.mmocore.gameserver.utils.Location;

public final class PlayerTemplateParser extends AbstractDirParser<PlayerTemplateHolder>
{
	private static final PlayerTemplateParser _instance = new PlayerTemplateParser();

	public static PlayerTemplateParser getInstance()
	{
		return _instance;
	}

	private PlayerTemplateParser()
	{
		super(PlayerTemplateHolder.getInstance());
	}

	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/pc_parameters/template_data/");
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "template_data.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			Race race = Race.valueOf(element.attributeValue("race").toLowerCase());
			Sex sex = Sex.valueOf(element.attributeValue("sex").toUpperCase());
			ClassType classtype = ClassType.valueOf(element.attributeValue("type").toUpperCase());

			StatAttributes min_attr = null;
			StatAttributes max_attr = null;
			StatAttributes base_attr = null;
			BaseArmorDefence arm_defence = null;
			BaseJewelDefence jewl_defence = null;

			StatsSet stats_set = new StatsSet();
			List<StartItem> start_items = new ArrayList<StartItem>();
			List<Location> start_locations = new ArrayList<Location>();
			TIntObjectHashMap<LvlUpData> lvl_up_data = new TIntObjectHashMap<LvlUpData>();

			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();

				if ("creation_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("start_equipments".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
							{
								if ("equipment".equalsIgnoreCase(e2.getName()))
								{

									int item_id = Integer.parseInt(e2.attributeValue("item_id"));
									long count = Long.parseLong(e2.attributeValue("count"));
									boolean equiped = Boolean.parseBoolean(e2.attributeValue("equiped"));
									start_items.add(new StartItem(item_id, count, equiped));
								}
							}
						}
						else if ("start_points".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
								if ("point".equalsIgnoreCase(e2.getName()))
									start_locations.add(Location.parse(e2));
						}
					}
				}
				else if ("stats_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if (("min_attributes".equalsIgnoreCase(e.getName())) || ("max_attributes".equalsIgnoreCase(e.getName())) || ("base_attributes".equalsIgnoreCase(e.getName())))
						{
							int _int = Integer.parseInt(e.attributeValue("int"));
							int str = Integer.parseInt(e.attributeValue("str"));
							int con = Integer.parseInt(e.attributeValue("con"));
							int men = Integer.parseInt(e.attributeValue("men"));
							int dex = Integer.parseInt(e.attributeValue("dex"));
							int wit = Integer.parseInt(e.attributeValue("wit"));
							StatAttributes attr = new StatAttributes(_int, str, con, men, dex, wit);
							if ("min_attributes".equalsIgnoreCase(e.getName()))
								min_attr = attr;
							else if ("max_attributes".equalsIgnoreCase(e.getName()))
								max_attr = attr;
							else if ("base_attributes".equalsIgnoreCase(e.getName()))
								base_attr = attr;
						}
						else if ("armor_defence".equalsIgnoreCase(e.getName()))
						{
							int chest = Integer.parseInt(e.attributeValue("chest"));
							int legs = Integer.parseInt(e.attributeValue("legs"));
							int helmet = Integer.parseInt(e.attributeValue("helmet"));
							int boots = Integer.parseInt(e.attributeValue("boots"));
							int gloves = Integer.parseInt(e.attributeValue("gloves"));
							int underwear = Integer.parseInt(e.attributeValue("underwear"));
							int cloak = Integer.parseInt(e.attributeValue("cloak"));
							arm_defence = new BaseArmorDefence(chest, legs, helmet, boots, gloves, underwear, cloak);
						}
						else if ("jewel_defence".equalsIgnoreCase(e.getName()))
						{
							int r_earring = Integer.parseInt(e.attributeValue("r_earring"));
							int l_earring = Integer.parseInt(e.attributeValue("l_earring"));
							int r_ring = Integer.parseInt(e.attributeValue("r_ring"));
							int l_ring = Integer.parseInt(e.attributeValue("l_ring"));
							int necklace = Integer.parseInt(e.attributeValue("necklace"));
							jewl_defence = new BaseJewelDefence(r_earring, l_earring, r_ring, l_ring, necklace);
						}
						else if ("base_stats".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
							{
								if ("stat_set".equalsIgnoreCase(e2.getName()))
								{
									stats_set.set(e2.attributeValue("name"), e2.attributeValue("value"));
								}
								else if ("regen_lvl_data".equalsIgnoreCase(e2.getName()))
								{
									for (Element e3 : e2.elements())
									{
										if ("lvl_data".equalsIgnoreCase(e3.getName()))
										{
											int lvl = Integer.parseInt(e3.attributeValue("lvl"));
											double hp = Double.parseDouble(e3.attributeValue("hp"));
											double mp = Double.parseDouble(e3.attributeValue("mp"));
											double cp = Double.parseDouble(e3.attributeValue("cp"));
											lvl_up_data.put(lvl, new LvlUpData(hp, mp, cp));
										}
									}
								}
							}
						}
					}
				}
			}
			PlayerTemplate template = new PlayerTemplate(stats_set, sex, min_attr, max_attr, base_attr, arm_defence, jewl_defence, start_locations, start_items, lvl_up_data);
			getHolder().addPlayerTemplate(race, classtype, sex, template);
		}
	}
}
