package org.mmocore.gameserver.model.items.listeners;

import org.mmocore.gameserver.data.xml.holder.OptionDataHolder;
import org.mmocore.gameserver.listener.inventory.OnEquipListener;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.network.serverpackets.SkillCoolTime;
import org.mmocore.gameserver.templates.OptionDataTemplate;

public final class ItemAugmentationListener implements OnEquipListener
{
	private static final ItemAugmentationListener _instance = new ItemAugmentationListener();

	public static ItemAugmentationListener getInstance()
	{
		return _instance;
	}

	@Override
	public void onUnequip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
			return;
		if (!item.isAugmented())
			return;

		Player player = actor.getPlayer();

		int stats[] = new int[2];
		stats[0] = 0x0000FFFF & item.getAugmentationId();
		stats[1] = item.getAugmentationId() >> 16;

		boolean sendList = false;
		for (int i : stats)
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
				continue;

			player.removeStatsOwner(template);

			for (Skill skill : template.getSkills())
			{
				sendList = true;
				player.removeSkill(skill);
			}

			player.removeTriggers(template);
		}

		if (sendList)
			player.sendSkillList();

		player.updateStats();
	}

	@Override
	public void onEquip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
			return;
		if (!item.isAugmented())
			return;

		Player player = actor.getPlayer();

		// При несоотвествии грейда аугмент не применяется
		if (player.getExpertisePenalty(item) > 0)
			return;

		int stats[] = new int[2];
		stats[0] = 0x0000FFFF & item.getAugmentationId();
		stats[1] = item.getAugmentationId() >> 16;

		boolean sendList = false;
		boolean sendReuseList = false;
		for (int i : stats)
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
				continue;

			player.addStatFuncs(template.getStatFuncs(template));

			for (Skill skill : template.getSkills())
			{
				sendList = true;
				player.addSkill(skill);
				if (player.isSkillDisabled(skill))
					sendReuseList = true;
			}

			player.addTriggers(template);
		}

		if (sendList)
			player.sendSkillList();

		if (sendReuseList)
			player.sendPacket(new SkillCoolTime(player));

		player.updateStats();
	}
}