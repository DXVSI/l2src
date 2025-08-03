package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.data.BoatHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.boat.Boat;
import org.mmocore.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class RequestGetOffShuttle extends L2GameClientPacket
{
	private int _shuttleId;
	private Location _location = new Location();

	@Override
	protected void readImpl()
	{
		_shuttleId = readD();
		_location.x = readD();
		_location.y = readD();
		_location.z = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Boat boat = BoatHolder.getInstance().getBoat(_shuttleId);
		if (boat == null || boat.isMoving)
		{
			player.sendActionFailed();
			return;
		}

		boat.oustPlayer(player, _location, false);
	}
}