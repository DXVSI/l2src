package npc.model;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

/**
 * Данный инстанс используется NPC 13193 в локации Seed of Destruction
 *
 * @author SYS
 */
public class FakeObeliskInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -6844518880448560169L;

	public FakeObeliskInstance(int objectId, NpcTemplate template)
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