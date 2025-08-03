package org.mmocore.gameserver.network.serverpackets.components;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 13:28/01.12.2010
 */
public interface IStaticPacket
{
	L2GameServerPacket packet(Player player);
}
