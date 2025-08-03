package npc.model;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

/**
 * Данный инстанс используется NPC Sandstorm в локации Hellbound
 *
 * @author SYS
 */
public class SandstormInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7444139675903683212L;

	public SandstormInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{}

	@Override
	public void onAction(Player player, boolean shift)
	{
		player.sendActionFailed();
	}
}