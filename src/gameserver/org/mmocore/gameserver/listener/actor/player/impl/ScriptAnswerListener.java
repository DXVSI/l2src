package org.mmocore.gameserver.listener.actor.player.impl;

import org.mmocore.commons.lang.reference.HardReference;
import org.mmocore.gameserver.listener.actor.player.OnAnswerListener;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.scripts.Scripts;

/**
 * @author VISTALL
 * @date 11:36/15.04.2011
 */
public class ScriptAnswerListener implements OnAnswerListener
{
	private HardReference<Player> _playerRef;
	private String _scriptName;
	private Object[] _arg;

	public ScriptAnswerListener(Player player, String scriptName, Object[] arg)
	{
		_scriptName = scriptName;
		_arg = arg;
		_playerRef = player.getRef();
	}

	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if (player == null)
			return;

		Scripts.getInstance().callScripts(player, _scriptName.split(":")[0], _scriptName.split(":")[1], _arg);
	}

	@Override
	public void sayNo()
	{
		//
	}
}
