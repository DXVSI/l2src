package org.mmocore.gameserver.model.entity.events.actions;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.model.GameObject;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.events.EventAction;
import org.mmocore.gameserver.model.entity.events.GlobalEvent;
import org.mmocore.gameserver.network.serverpackets.PlaySound;

/**
 * @author VISTALL
 * @date 16:25/06.01.2011
 */
public class PlaySoundAction implements EventAction
{
	private int _range;
	private String _sound;
	private PlaySound.Type _type;

	public PlaySoundAction(int range, String s, PlaySound.Type type)
	{
		_range = range;
		_sound = s;
		_type = type;
	}

	@Override
	public void call(GlobalEvent event)
	{
		GameObject object = event.getCenterObject();
		PlaySound packet = null;
		if (object != null)
			packet = new PlaySound(_type, _sound, 1, object.getObjectId(), object.getLoc());
		else
			packet = new PlaySound(_type, _sound, 0, 0, 0, 0, 0);

		GArray<Player> players = event.broadcastPlayers(_range);
		for (Player player : players)
			if (player != null)
				player.sendPacket(packet);
	}
}
