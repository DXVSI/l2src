package npc.model.residences;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 17:49/13.07.2011
 */
public class TeleportSiegeGuardInstance extends SiegeGuardInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 3814356141829950745L;

	public TeleportSiegeGuardInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

	}
}
