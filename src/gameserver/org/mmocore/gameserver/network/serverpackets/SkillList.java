package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Skill.SkillType;
import org.mmocore.gameserver.tables.SkillTreeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * format d (dddc)
 */
public class SkillList extends L2GameServerPacket
{
	private List<Skill> _skills;
	private boolean canEnchant;
	private Player activeChar;
	private int _learnedSkill;

	public SkillList(Player p)
	{
		_skills = new ArrayList<Skill>(p.getAllSkills());
		canEnchant = p.getTransformation() == 0;
		activeChar = p;
		_learnedSkill = 0;
	}

	public SkillList(Player p, int learnedSkill)
	{
		_skills = new ArrayList<Skill>(p.getAllSkills());
		canEnchant = p.getTransformation() == 0;
		activeChar = p;
		_learnedSkill = learnedSkill;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x5f);
		writeD(_skills.size());

		for (Skill temp : _skills)
		{
			writeD(temp.isActive() || temp.isToggle() ? 0 : 1); // deprecated?
			                                                    // клиентом
			                                                    // игнорируется
			writeD(temp.getDisplayLevel());
			writeD(temp.getDisplayId());
			writeD(temp.getSkillType() == SkillType.EMDAM ? temp.getDisplayId() : -1);
			writeC(activeChar.isUnActiveSkill(temp.getId()) ? 0x01 : 0x00); // иконка
			                                                                // скилла
			                                                                // серая
			                                                                // если
			                                                                // не
			                                                                // 0
			writeC(canEnchant ? SkillTreeTable.isEnchantable(temp) : 0); // для
			                                                             // заточки:
			                                                             // если
			                                                             // 1
			                                                             // скилл
			                                                             // можно
			                                                             // точить
		}
		writeD(_learnedSkill);
	}
}