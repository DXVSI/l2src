package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.Creature;

/**
 * @author ALF
 * @modified KilRoy
 * @date 29.07.2012
 */
public class ExTacticalSign extends L2GameServerPacket
{
	private Creature targetId;
	private int signId;

	public static int SIGN_NONE = 0;
	public static int SIGN_STAR = 1;
	public static int SIGN_HEART = 2;
	public static int SIGN_MOON = 3;
	public static int SIGN_CROSS = 4;

	public ExTacticalSign(Creature target, int sign)
	{
		targetId = target;
		signId = sign;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x100);
		writeD(targetId.getObjectId());
		writeD(signId);
	}
}