package npc.model;

import java.util.Map;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.instancemanager.DimensionalRiftManager;
import org.mmocore.gameserver.instancemanager.DimensionalRiftManager.DimensionalRiftRoom;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.DelusionChamber;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.party.Party;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

/**
 * @author pchayka
 */

public final class DelustionGatekeeperInstance extends NpcInstance
{
	private static final long serialVersionUID = 1032770507263157511L;

	public DelustionGatekeeperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.startsWith("enterDC"))
		{
			int izId = Integer.parseInt(command.substring(8));
			int type = izId - 120;
			Map<Integer, DimensionalRiftRoom> rooms = DimensionalRiftManager.getInstance().getRooms(type);
			if(rooms == null)
			{
				player.sendPacket(Msg.SYSTEM_ERROR);
				return;
			}
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(izId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(izId))
			{
				Party party = player.getParty();
				if(party != null)
					new DelusionChamber(party, type, Rnd.get(1, rooms.size() - 1));
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
}