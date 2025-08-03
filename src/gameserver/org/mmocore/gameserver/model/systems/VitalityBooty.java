package org.mmocore.gameserver.model.systems;

import gnu.trove.list.array.TIntArrayList;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.MonsterInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author ALF
 * @date 23.10.2012
 */
public class VitalityBooty
{
	private static final Logger _log = LoggerFactory.getLogger(VitalityBooty.class);

	private static GArray<VitalityBootyItem> _items = new GArray<VitalityBootyItem>();

	private static final int CHANCE = 3;

	public static void load()
	{
		_log.info("VitalityBooty: Loading items data...");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);

		File file = new File(Config.DATAPACK_ROOT, "data/asc/model/systems/VitalityBooty.xml");
		Document doc = null;
		int id = 0, level = 0;

		if (file.exists())
		{
			try
			{
				doc = factory.newDocumentBuilder().parse(file);
			}
			catch (Exception e)
			{
				_log.warn("Could not parse VitalityBooty.xml file: " + e.getMessage(), e);
				return;
			}

			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("item".equalsIgnoreCase(d.getNodeName()))
						{
							NamedNodeMap attrs = d.getAttributes();
							Node att;

							att = attrs.getNamedItem("id");
							id = Integer.parseInt(att.getNodeValue());

							att = attrs.getNamedItem("level");
							level = Integer.parseInt(att.getNodeValue());

							VitalityBootyItem item = new VitalityBootyItem(id, level);
							_items.add(item);
						}
					}
				}
			}
		}

		_log.info("VitalityBooty: Loaded " + _items.size() + " items data...");
	}

	// TODO: Узнать как работает это на оффе.
	public static void rewardPlayer(Player p, MonsterInstance mob)
	{
		if (p == null)
			return;

		// Наличие очков Энергии также дает шанс получения дополнительных трофеев
		if (p.getVitality().getPoints() == 0)
			return;

		// Это мешки с оружием, доспехами и мешки усиления. Ранг мешка зависит от уровня монстров.
		int maxLevel = mob.getLevel() + 10;
		int minLevel = mob.getLevel() - 10;

		TIntArrayList items = new TIntArrayList();

		// Находим список итемов которые подойдут по уровню
		for (VitalityBootyItem i : _items)
		{
			if (i.getLevel() > minLevel && i.getLevel() < maxLevel)
				items.add(i.getItemId());
		}

		if (Rnd.chance(CHANCE) && items.size() != 0)
		{
			int offset = Rnd.get(items.size());
			// TODO: Возможно надо перенести в rollRewards
			mob.dropItem(p, items.get(offset), 1);
		}
	}
}
