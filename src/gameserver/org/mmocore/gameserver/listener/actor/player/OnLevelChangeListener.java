package org.mmocore.gameserver.listener.actor.player;

import org.mmocore.gameserver.listener.PlayerListener;
import org.mmocore.gameserver.model.Player;

/**
 * @author : Ragnarok
 * @date : 28.03.12 16:54
 */
public interface OnLevelChangeListener extends PlayerListener
{
	public void onLevelChange(Player player, int oldLvl, int newLvl);
}
