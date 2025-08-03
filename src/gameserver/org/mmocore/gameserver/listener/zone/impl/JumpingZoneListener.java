package org.mmocore.gameserver.listener.zone.impl;

import org.mmocore.commons.lang.reference.HardReference;
import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.network.serverpackets.ExNotifyFlyMoveStart;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author ALF
 * @date 13.07.2012
 */
public class JumpingZoneListener implements OnZoneEnterLeaveListener
{
	public static final OnZoneEnterLeaveListener STATIC = new JumpingZoneListener();

	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		if (!actor.isPlayer())
			return;

		Player player = actor.getPlayer();
		if (!player.isAwaking())
			return;

		if (player.getTransformation() != 0)
			return;
		
		if (!player.getPets().isEmpty())
		{
			player.sendPacket(SystemMsg.YOU_MAY_NOT_USE_SAYUNE_WHILE_PET_OR_SUMMONED_PET_IS_OUT);
			return;
		}

		player.sendPacket(ExNotifyFlyMoveStart.STATIC);
		ThreadPoolManager.getInstance().schedule(new NotifyPacketTask(zone, player), 500L);
	}

	@Override
	public void onZoneLeave(Zone zone, Creature cha)
	{
	}

	public static class NotifyPacketTask extends RunnableImpl
	{
		private final Zone _zone;
		private final HardReference<Player> _playerRef;

		public NotifyPacketTask(Zone zone, Player player)
		{
			_zone = zone;
			_playerRef = player.getRef();
		}

		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
				return;

			if (!player.isInZone(Zone.ZoneType.JUMPING))
				return;

			player.sendPacket(ExNotifyFlyMoveStart.STATIC);
			ThreadPoolManager.getInstance().schedule(new NotifyPacketTask(_zone, player), 500L);
		}
	}
}
