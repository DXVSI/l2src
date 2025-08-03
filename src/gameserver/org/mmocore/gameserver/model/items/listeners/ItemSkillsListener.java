package org.mmocore.gameserver.model.items.listeners;

import org.mmocore.gameserver.listener.inventory.OnEquipListener;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.network.serverpackets.SkillCoolTime;
import org.mmocore.gameserver.stats.Formulas;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.item.ItemTemplate;

public final class ItemSkillsListener implements OnEquipListener
{
	private static final ItemSkillsListener _instance = new ItemSkillsListener();

	public static ItemSkillsListener getInstance()
	{
		return _instance;
	}

	@Override
	public void onUnequip(int slot, ItemInstance item, Playable actor)
	{
		Player player = (Player) actor;

		Skill[] itemSkills = null;
		Skill enchant4Skill = null;

		ItemTemplate it = item.getTemplate();

		itemSkills = it.getAttachedSkills();

		enchant4Skill = it.getEnchant4Skill();

		player.removeTriggers(it);

		if (itemSkills != null && itemSkills.length > 0)
			for (Skill itemSkill : itemSkills)
				if (itemSkill.getId() >= 26046 && itemSkill.getId() <= 26048)
				{
					int level = player.getSkillLevel(itemSkill.getId());
					int newlevel = level - 1;
					if (newlevel > 0)
						player.addSkill(SkillTable.getInstance().getInfo(itemSkill.getId(), newlevel), false);
					else
						player.removeSkillById(itemSkill.getId());
				}
				else
					player.removeSkill(itemSkill, false);

		if (enchant4Skill != null)
			player.removeSkill(enchant4Skill, false);

		if (itemSkills.length > 0 || enchant4Skill != null)
		{
			player.sendSkillList();
			player.updateStats();
		}
	}

	@Override
	public void onEquip(int slot, ItemInstance item, Playable actor)
	{
		Player player = (Player) actor;

		Skill[] itemSkills = null;
		Skill enchant4Skill = null;

		ItemTemplate it = item.getTemplate();

		itemSkills = it.getAttachedSkills();

		if (item.getEnchantLevel() >= 4)
			enchant4Skill = it.getEnchant4Skill();

		// Для оружия при несоотвествии грейда скилы не выдаем
		if (it.getType2() == ItemTemplate.TYPE2_WEAPON && player.getWeaponsExpertisePenalty() > 0)
			return;

		player.addTriggers(it);

		boolean needSendInfo = false;
		if (itemSkills.length > 0)
			for (Skill itemSkill : itemSkills)
				if (itemSkill.getId() >= 26046 && itemSkill.getId() <= 26048)
				{
					int level = player.getSkillLevel(itemSkill.getId());
					int newlevel = level;
					if (level > 0)
					{
						if (SkillTable.getInstance().getInfo(itemSkill.getId(), level + 1) != null)
							newlevel = level + 1;
					}
					else
						newlevel = 1;
					if (newlevel != level)
						player.addSkill(SkillTable.getInstance().getInfo(itemSkill.getId(), newlevel), false);
				}
				else if (player.getSkillLevel(itemSkill.getId()) < itemSkill.getLevel())
				{
					player.addSkill(itemSkill, false);

					if (itemSkill.isActive())
					{
						long reuseDelay = Formulas.calcSkillReuseDelay(player, itemSkill);
						reuseDelay = Math.min(reuseDelay, 30000);

						if (reuseDelay > 0 && !player.isSkillDisabled(itemSkill))
						{
							player.disableSkill(itemSkill, reuseDelay);
							needSendInfo = true;
						}
					}
				}

		if (enchant4Skill != null)
			player.addSkill(enchant4Skill, false);

		if (itemSkills.length > 0 || enchant4Skill != null)
		{
			player.sendSkillList();
			player.updateStats();
			if (needSendInfo)
				player.sendPacket(new SkillCoolTime(player));
		}
	}
}