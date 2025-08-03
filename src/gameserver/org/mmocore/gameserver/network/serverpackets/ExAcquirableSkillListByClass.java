package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.data.xml.holder.SkillAcquireHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.SkillLearn;
import org.mmocore.gameserver.tables.SkillTable;

import java.util.Collection;

/**
 * @author ALF
 * @date 17.07.2012
 */
public class ExAcquirableSkillListByClass extends L2GameServerPacket
{
	private final Collection<SkillLearn> allskills;

	public ExAcquirableSkillListByClass(Player player)
	{
		allskills = SkillAcquireHolder.getInstance().getAvailableAllSkills(player);
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0xFA);

		writeD(allskills.size());
		for (SkillLearn sk : allskills)
		{
			Skill skill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());

			if (skill == null)
				continue;

			writeD(sk.getId());
			writeD(sk.getLevel());
			writeD(sk.getCost());
			writeH(sk.getMinLevel());
			// writeH(0x00); // Tauti
			boolean consumeItem = sk.getItemId() > 0;
			writeD(consumeItem ? 1 : 0);
			if (consumeItem)
			{
				writeD(sk.getItemId());
				writeQ(sk.getItemCount());
			}
			/*
			 * if(skill.getRelationSkills() != null) { int[] skills = skill.getRelationSkills(); writeD(skills.length); for(int relation : skills) {
			 * writeD(relation); writeD(1); } } else
			 */
			writeD(0x00); // Скилы которые удаляет [dd] НЕ СУЩЕСТВИТЕЛЬНО!!!
			              // Именно ТУТ.
		}
	}

}
