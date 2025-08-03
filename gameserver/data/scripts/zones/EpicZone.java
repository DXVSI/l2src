package zones;

import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.Location;
import org.mmocore.gameserver.utils.ReflectionUtils;

public class EpicZone implements ScriptFile
{
	private static ZoneListener _zoneListener;

	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone("[queen_ant_epic]");
		zone.addListener(_zoneListener);
	}

	@Override
	public void onReload()
	{

	}

	@Override
	public void onShutdown()
	{

	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(zone.getParams() == null || !cha.isPlayable() || cha.getPlayer().isGM())
				return;
			if(cha.getLevel() > zone.getParams().getInteger("levelLimit"))
			{
				if(cha.isPlayer())
					cha.getPlayer().sendMessage(new CustomMessage("scripts.zones.epic.banishMsg", (Player) cha));
				cha.teleToLocation(Location.parseLoc(zone.getParams().getString("tele")));
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{

		}
	}
}
