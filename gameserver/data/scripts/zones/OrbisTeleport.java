package zones;

import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.Location;
import org.mmocore.gameserver.utils.ReflectionUtils;

public class OrbisTeleport implements ScriptFile
{
	private static final String TELEPORT_ZONE_NAME = "[Orbis_0_to_1]";
	private static final Location[] TELEPORT_LOC = { new Location(213983, 53250, -8176), new Location(198022, 90032, -192) };
	
	
	private static final String[] zones = { "[Orbis_0_to_1]", "[Orbis_1_to_0]" };

	private static ZoneListener _zoneListener;

	private void init()
	{	
		_zoneListener = new ZoneListener();

		for (String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
	}

	@Override
	public void onLoad()
	{
		init();
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
			if (zone == null)
				return;

			if (cha == null)
				return;

			if (zone.getName().equalsIgnoreCase("[Orbis_0_to_1]"))
				cha.teleToLocation(TELEPORT_LOC[0]);
			else if (zone.getName().equalsIgnoreCase("[Orbis_1_to_0]"))
				cha.teleToLocation(TELEPORT_LOC[1]);
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}