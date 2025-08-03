package org.mmocore.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.SkillLearn;
import org.mmocore.gameserver.tables.SkillTable;

/**
 * @author ALF
 */
public class ExAcquireSkillInfo extends L2GameServerPacket
{
	private SkillLearn _learn;
	private List<Require> _reqs = new ArrayList<Require>(1);
	private List<Integer> _dels = new ArrayList<Integer>(10);
	private Skill _skill;

	public ExAcquireSkillInfo(SkillLearn learn)
	{
		_learn = learn;
		if (_learn.getItemId() != 0)
			_reqs.add(new Require(_learn.getItemId(), _learn.getItemCount()));

		// Разницы в ЛвЛе нету, так как список удаляемых скилов не зависит от
		// лвла скила
		// 1 - самый оптимиальный вариант так как нету необходиомсти
		// конвертирования
		// лвла скила с клиентского представления в серверное.
		_skill = SkillTable.getInstance().getInfo(learn.getId(), 1);

		// Не раельно, но всё же....
		if (_skill != null)
			if (_skill.getRelationSkills() != null)
			{
				for (int id : _skill.getRelationSkills())
					_dels.add(id);
			}
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xFC);
		writeD(_learn.getId());
		writeD(_learn.getLevel());
		writeD(_learn.getCost());
		writeH(_learn.getMinLevel());
		writeD(_reqs.size());
		for (Require temp : _reqs)
		{
			writeD(temp.itemId);
			writeQ(temp.count);
		}
		writeD(_dels.size());
		for (int id : _dels)
		{
			writeD(id);
			writeD(1); // Тут лвл скила, но существителин ли он тут??? оО
		}
	}

	private static class Require
	{
		public int itemId;
		public long count;

		public Require(int pItemId, long pCount)
		{
			itemId = pItemId;
			count = pCount;
		}
	}
}
