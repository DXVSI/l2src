package npc.model;

import instances.HeartInfinityAttack;
import instances.HeartInfinityDefence;

import org.mmocore.gameserver.instancemanager.SoIManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * @author pchayka
 */

public final class AbyssGazeInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8237015750011165364L;
	private static final int ekimusIzId = 121;
	private static final int hoidefIzId = 122;

	public AbyssGazeInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.startsWith("request_permission"))
		{
			if(SoIManager.getCurrentStage() == 2 || SoIManager.getCurrentStage() == 5)
			{
				showChatWindow(player, "default/32540-2.htm");
				return;
			}
			else if(SoIManager.getCurrentStage() == 3 && SoIManager.isSeedOpen())
			{
				showChatWindow(player, "default/32540-3.htm");
				return;
			}
			else
			{
				showChatWindow(player, "default/32540-1.htm");
				return;
			}
		}
		else if(command.equalsIgnoreCase("request_ekimus"))
		{
			if(SoIManager.getCurrentStage() == 2)
			{
				Reflection r = player.getActiveReflection();
				if(r != null)
				{
					if(player.canReenterInstance(ekimusIzId))
						player.teleToLocation(r.getTeleportLoc(), r);
				}
				else if(player.canEnterInstance(ekimusIzId))
					ReflectionUtils.enterReflection(player, new HeartInfinityAttack(), ekimusIzId);
			}
		}
		else if(command.equalsIgnoreCase("enter_seed"))
		{
			if(SoIManager.getCurrentStage() == 3)
			{
				SoIManager.teleportInSeed(player);
				return;
			}
		}
		else if(command.equalsIgnoreCase("hoi_defence"))
		{
			if(SoIManager.getCurrentStage() == 5)
			{
				Reflection r = player.getActiveReflection();
				if(r != null)
				{
					if(player.canReenterInstance(hoidefIzId))
						player.teleToLocation(r.getTeleportLoc(), r);
				}
				else if(player.canEnterInstance(hoidefIzId))
					ReflectionUtils.enterReflection(player, new HeartInfinityDefence(), hoidefIzId);
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
}