package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ActionFail;
import org.mmocore.gameserver.network.serverpackets.CharMoveToLocation;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.utils.Location;

// cdddddd(d)
public class MoveBackwardToLocation extends L2GameClientPacket
{
	private Location _targetLoc = new Location();
	private Location _originLoc = new Location();
	private int _moveMovement;

	/**
	 * packet type id 0x0f
	 */
	@Override
	protected void readImpl()
	{
		_targetLoc.x = readD();
		_targetLoc.y = readD();
		_targetLoc.z = readD();
		_originLoc.x = readD();
		_originLoc.y = readD();
		_originLoc.z = readD();
		if (_buf.hasRemaining())
			_moveMovement = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.setActive();

		if (System.currentTimeMillis() - activeChar.getLastMovePacket() < Config.MOVE_PACKET_DELAY)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.setLastMovePacket();

		if (activeChar.isTeleporting())
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isFrozen())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_MOVE_WHILE_FROZEN, ActionFail.STATIC);
			return;
		}

		if (activeChar.isInObserverMode())
		{
			if (activeChar.getOlympiadObserveGame() == null)
				activeChar.sendActionFailed();
			else
				activeChar.sendPacket(new CharMoveToLocation(activeChar.getObjectId(), _originLoc, _targetLoc));
			return;
		}

		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.getTeleMode() > 0)
		{
			if (activeChar.getTeleMode() == 1)
				activeChar.setTeleMode(0);
			activeChar.sendActionFailed();
			activeChar.teleToLocation(_targetLoc);
			return;
		}

		if (activeChar.isInFlyingTransform())
			_targetLoc.z = Math.min(5950, Math.max(50, _targetLoc.z)); // В
			                                                           // летающей
			                                                           // трансформе
			                                                           // нельзя
			                                                           // летать
			                                                           // ниже,
			                                                           // чем 0,
			                                                           // и
			                                                           // выше,
			                                                           // чем
			                                                           // 6000

		ThreadPoolManager.getInstance().executePathfind(new StartMoveTask(activeChar, _targetLoc, _moveMovement != 0));
	}

	public static class StartMoveTask implements Runnable
	{
		private Player _player;
		private Location _loc;
		private boolean _pathfind;

		public StartMoveTask(Player player, Location loc, boolean pathfind)
		{
			_player = player;
			_loc = loc;
			_pathfind = pathfind;
		}

		@Override
		public void run()
		{
			_player.moveToLocation(_loc, 0, _pathfind && !_player.getVarB("no_pf"));
		}
	}
}