package org.mmocore.gameserver.model.items.listeners;

import org.mmocore.gameserver.data.xml.holder.OptionDataHolder;
import org.mmocore.gameserver.listener.inventory.OnEquipListener;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.stats.triggers.TriggerInfo;
import org.mmocore.gameserver.templates.OptionDataTemplate;

/**
 * @author VISTALL
 * @date 19:34/19.05.2011
 */
public final class ItemEnchantOptionsListener implements OnEquipListener
{
	private static final ItemEnchantOptionsListener _instance = new ItemEnchantOptionsListener();

	public static ItemEnchantOptionsListener getInstance()
	{
		return _instance;
	}

	@Override
	public void onEquip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
			return;
		Player player = actor.getPlayer();

		boolean needSendInfo = false;
		for (int i : item.getEnchantOptions())
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
				continue;

			player.addStatFuncs(template.getStatFuncs(template));
			for (Skill skill : template.getSkills())
			{
				player.addSkill(skill, false);
				needSendInfo = true;
			}
			for (TriggerInfo triggerInfo : template.getTriggerList())
				player.addTrigger(triggerInfo);
		}

		if (needSendInfo)
			player.sendSkillList();
		player.sendChanges();
	}

	@Override
	public void onUnequip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
			return;

		Player player = actor.getPlayer();

		boolean needSendInfo = false;
		for (int i : item.getEnchantOptions())
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
				continue;

			player.removeStatsOwner(template);
			for (Skill skill : template.getSkills())
			{
				player.removeSkill(skill, false);
				needSendInfo = true;
			}
			for (TriggerInfo triggerInfo : template.getTriggerList())
				player.removeTrigger(triggerInfo);
		}

		if (needSendInfo)
			player.sendSkillList();
		player.sendChanges();
	}
}
