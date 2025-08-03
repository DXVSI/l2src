package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.data.BoatHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.boat.Boat;
import org.mmocore.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class RequestGetOnShuttle extends L2GameClientPacket
{
	private int _shuttleId;
	private Location _loc = new Location();

	@Override
	protected void readImpl()
	{
		_shuttleId = readD();
		_loc.x = readD();
		_loc.y = readD();
		_loc.z = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Boat boat = BoatHolder.getInstance().getBoat(_shuttleId);
		if (boat == null)
			return;

		player.setStablePoint(boat.getReturnLoc());
		boat.addPlayer(player, _loc);
	}
}