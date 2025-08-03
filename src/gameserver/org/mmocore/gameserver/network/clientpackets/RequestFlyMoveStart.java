package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.data.xml.holder.JumpTracksHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.model.Zone.ZoneType;
import org.mmocore.gameserver.network.serverpackets.ExFlyMove;
import org.mmocore.gameserver.network.serverpackets.ExFlyMoveBroadcast;
import org.mmocore.gameserver.network.serverpackets.FlyToLocation;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.templates.jump.JumpTrack;
import org.mmocore.gameserver.templates.jump.JumpWay;
import org.mmocore.gameserver.utils.Location;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class RequestFlyMoveStart extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		//
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if (activeChar.getTransformation() > 0)
			return;
		
		if (!activeChar.getPets().isEmpty())
		{
			activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_USE_SAYUNE_WHILE_PET_OR_SUMMONED_PET_IS_OUT);
			return;
		}
		
		Zone zone = activeChar.getZone(ZoneType.JUMPING);
		if (zone == null)
			return;

		JumpTrack track = JumpTracksHolder.getInstance().getTrack(zone.getTemplate().getJumpTrackId());
		if (track == null)
			return;

		Location destLoc = track.getStartLocation();
		activeChar.sendPacket(new FlyToLocation(activeChar, destLoc, FlyToLocation.FlyType.DUMMY, 0));

		JumpWay way = track.getWay(0);
		if (way == null)
			return;

		activeChar.sendPacket(new ExFlyMove(activeChar.getObjectId(), way.getPoints(), track.getId()));
		activeChar.broadcastPacketToOthers(new ExFlyMoveBroadcast(activeChar, 2, destLoc));
		activeChar.setVar("@safe_jump_loc", activeChar.getLoc().toXYZString(), -1);
		activeChar.setCurrentJumpTrack(track);
		activeChar.setCurrentJumpWay(way);
	}
}