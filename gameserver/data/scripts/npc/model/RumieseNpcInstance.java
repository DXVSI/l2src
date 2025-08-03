package npc.model;

import instances.IsthinaNormal;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ItemFunctions;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy
 * Для работы с инстами - истхины
 * У НПСов 33151, 33293 (Rumiese)
 * TODO[K] - Раскоментить всё если допишу инст+Аи хард мода Истхины + Сообразить награду при её смерти
 */
public final class RumieseNpcInstance extends NpcInstance
{
	private static final long serialVersionUID = 5984176213940365077L;
	private static final int normalIsthinaInstId = 169;
	private static final int hardIsthinaInstId = 170;

	public RumieseNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_normalisthina"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(normalIsthinaInstId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(normalIsthinaInstId))
				ReflectionUtils.enterReflection(player, new IsthinaNormal(), normalIsthinaInstId);
		}
		else if(command.equalsIgnoreCase("request_hardisthina"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(hardIsthinaInstId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(hardIsthinaInstId))
				ReflectionUtils.enterReflection(player, new IsthinaNormal(), hardIsthinaInstId);
		}
		else if(command.equalsIgnoreCase("request_takemyprize"))
		{}
		else if(command.equalsIgnoreCase("request_Device"))
		{
			if(ItemFunctions.getItemCount(player, 17608) > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no.htm");
				return;
			}
			Functions.addItem(player, 17608, 1);
			showChatWindow(player, "default/" + getNpcId() + "-ok.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}
}