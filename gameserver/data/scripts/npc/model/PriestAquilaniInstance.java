package npc.model;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.NpcHtmlMessage;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;

import quests._10288_SecretMission;

/**
 * @author pchayka
 */
public class PriestAquilaniInstance extends NpcInstance
{

	/**
	 *
	 */
	private static final long serialVersionUID = -1300832727490524633L;

	public PriestAquilaniInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if(player.getQuestState(_10288_SecretMission.class) != null && player.getQuestState(_10288_SecretMission.class).isCompleted())
		{
			player.sendPacket(new NpcHtmlMessage(player, this, "default/32780-1.htm", val));
			return;
		}
		else
		{
			player.sendPacket(new NpcHtmlMessage(player, this, "default/32780.htm", val));
			return;
		}
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("teleport"))
		{
			player.teleToLocation(new Location(118833, -80589, -2688));
			return;
		}
		else
			super.onBypassFeedback(player, command);
	}
}