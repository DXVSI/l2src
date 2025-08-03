package org.mmocore.gameserver.handler.bypass;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.NpcInstance;

/**
 * @author VISTALL
 * @date 15:54/12.07.2011
 */
public interface IBypassHandler
{
	String[] getBypasses();

	void onBypassFeedback(NpcInstance npc, Player player, String command);
}
