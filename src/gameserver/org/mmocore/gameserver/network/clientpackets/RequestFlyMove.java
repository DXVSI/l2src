package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ExFlyMove;
import org.mmocore.gameserver.network.serverpackets.ExFlyMoveBroadcast;
import org.mmocore.gameserver.templates.jump.JumpPoint;
import org.mmocore.gameserver.templates.jump.JumpTrack;
import org.mmocore.gameserver.templates.jump.JumpWay;
import org.mmocore.gameserver.utils.Location;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public final class RequestFlyMove extends L2GameClientPacket
{
	private int _nextWayId;

	@Override
	protected void readImpl()
	{
		_nextWayId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		JumpWay way = activeChar.getCurrentJumpWay();
		if (way == null)
		{
			activeChar.onJumpingBreak();
			return;
		}

		JumpPoint point = way.getJumpPoint(_nextWayId);
		if (point == null)
		{
			activeChar.onJumpingBreak();
			return;
		}

		Location destLoc = point.getLocation();
		activeChar.broadcastPacketToOthers(new ExFlyMoveBroadcast(activeChar, 2, destLoc));
		activeChar.setLoc(destLoc);

		JumpTrack track = activeChar.getCurrentJumpTrack();
		if (track == null)
		{
			activeChar.onJumpingBreak();
			return;
		}

		JumpWay nextWay = track.getWay(_nextWayId);
		if (nextWay == null)
		{
			activeChar.onJumpingBreak();
			return;
		}

		activeChar.sendPacket(new ExFlyMove(activeChar.getObjectId(), nextWay.getPoints(), track.getId()));
		activeChar.setCurrentJumpWay(nextWay);
	}
}