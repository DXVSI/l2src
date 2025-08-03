package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.commons.lang.ArrayUtils;
import org.mmocore.gameserver.data.xml.holder.SkillAcquireHolder;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.SkillLearn;
import org.mmocore.gameserver.model.base.AcquireType;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.AcquireSkillInfo;
import org.mmocore.gameserver.network.serverpackets.ExAcquireSkillInfo;
import org.mmocore.gameserver.tables.SkillTable;

/**
 * Reworked: VISTALL
 */
public class RequestAquireSkillInfo extends L2GameClientPacket
{
	private int _id;
	private int _level;
	private AcquireType _type;

	@Override
	protected void readImpl()
	{
		_id = readD();
		_level = readD();
		_type = ArrayUtils.valid(AcquireType.VALUES, readD());
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null || player.getTransformation() != 0 || SkillTable.getInstance().getInfo(_id, _level) == null || _type == null)
			return;

		NpcInstance trainer = player.getLastNpc();
		if ((trainer == null || player.getDistance(trainer.getX(), trainer.getY()) > Creature.INTERACTION_DISTANCE) && !player.isGM() && _type != AcquireType.NORMAL)
			return;

		SkillLearn skillLearn = SkillAcquireHolder.getInstance().getSkillLearn(player, _id, _level, _type);
		if (skillLearn == null)
			return;
		if (_type == AcquireType.NORMAL)
			sendPacket(new ExAcquireSkillInfo(skillLearn));
		else
			sendPacket(new AcquireSkillInfo(_type, skillLearn));
	}
}