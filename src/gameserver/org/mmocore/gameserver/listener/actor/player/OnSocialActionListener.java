package org.mmocore.gameserver.listener.actor.player;

import org.mmocore.gameserver.listener.PlayerListener;
import org.mmocore.gameserver.model.GameObject;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.clientpackets.RequestActionUse.Action;

/**
 * Listener for social actions performed by player
 * 
 * @author Yorie
 */
public interface OnSocialActionListener extends PlayerListener
{
	public void onSocialAction(Player player, GameObject target, Action action);
}