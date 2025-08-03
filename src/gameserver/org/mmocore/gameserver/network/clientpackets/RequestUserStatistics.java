package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.gameserver.instancemanager.WorldStatisticsManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.worldstatistics.CharacterStatisticElement;
import org.mmocore.gameserver.network.serverpackets.ExLoadStatUser;

import java.util.List;

/**
 * @author ALF
 * @modified KilRoy
 * @data 08.08.2012
 */
public class RequestUserStatistics extends L2GameClientPacket
{

	@Override
	protected void readImpl() throws Exception
	{
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();

		if (player == null)
			return;

		List<CharacterStatisticElement> stat = WorldStatisticsManager.getInstance().getCurrentStatisticsForPlayer(player.getObjectId());
		player.sendPacket(new ExLoadStatUser(stat));
	}
}