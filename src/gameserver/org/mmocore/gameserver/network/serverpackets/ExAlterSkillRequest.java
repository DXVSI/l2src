package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.Skill;

// Это кнопка связана с комбо ударами!или ударами в прыжке!судя по мувикам
public class ExAlterSkillRequest extends L2GameServerPacket
{

	private Skill _skills;
	private int _time;

	public ExAlterSkillRequest(Skill id, int time)
	{
		_skills = id;
		_time = time;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x114);
		// ddd
		writeD(_skills.getId());// id скила (вроде как связано с ид скила)
		writeD(0); // хз
		writeD(_time);// Время показа кнопки в сек
	}
}