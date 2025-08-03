package npc.model;

import org.mmocore.gameserver.instancemanager.SoDManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * @author pchayka
 */

public final class AllenosInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -4745781285002656385L;
	private static final int tiatIzId = 110;

	public AllenosInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("enter_seed"))
		{
			// Время открытого SoD прошло
			if(SoDManager.isAttackStage())
			{
				Reflection r = player.getActiveReflection();
				if(r != null)
				{
					if(player.canReenterInstance(tiatIzId))
						player.teleToLocation(r.getTeleportLoc(), r);
				}
				else if(player.canEnterInstance(tiatIzId))
					ReflectionUtils.enterReflection(player, tiatIzId);
			}
			else
				SoDManager.teleportIntoSeed(player);
		}
		else
			super.onBypassFeedback(player, command);
	}
}