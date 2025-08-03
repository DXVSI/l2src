package npc.model;

import instances.MemoryOfDisaster;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ReflectionUtils;

public class CellphineInstance extends NpcInstance
{
	/**
	* 
	*/
	private static final long serialVersionUID = 6794289436944932326L;
	private static final int INSTANCE_ID = 200;

	public CellphineInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("Call_Hermuncus"))
		{
			if(player.getLevel() >= 85 && !player.isAwaking() && player.getVar("hermunkus") == null)
			{
				Reflection r = player.getActiveReflection();
				if(r != null)
				{
					if(player.canReenterInstance(INSTANCE_ID))
						player.teleToLocation(r.getTeleportLoc(), r);
				}
				else if(player.canEnterInstance(INSTANCE_ID))
				{
					ReflectionUtils.enterReflection(player, new MemoryOfDisaster(player), INSTANCE_ID);
					player.setVar("hermunkus", 1, -1);
				}
				else
					showChatWindow(player, "default/33477-no.htm");
			}
			else
				showChatWindow(player, "default/33477-no.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}
}
