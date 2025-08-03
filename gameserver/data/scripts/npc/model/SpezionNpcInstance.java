package npc.model;

import instances.SpezionNormal;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ItemFunctions;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * @author cruel
 */
public final class SpezionNpcInstance extends NpcInstance
{
	public SpezionNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("normal_spezion"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(159))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(159))
				ReflectionUtils.enterReflection(player, new SpezionNormal(), 159);
		}
		else
			super.onBypassFeedback(player, command);
	}
}