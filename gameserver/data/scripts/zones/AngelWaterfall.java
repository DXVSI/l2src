package zones;

import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.Location;
import org.mmocore.gameserver.utils.ReflectionUtils;

public class AngelWaterfall implements ScriptFile
{
	private static final String TELEPORT_ZONE_NAME = "[25_20_telzone_to_magmeld]";
	private static final Location TELEPORT_LOC = new Location(207559, 86429, -1000);

	private static ZoneListener _zoneListener;

	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(zone == null)
				return;

			if(cha == null)
				return;

			cha.teleToLocation(TELEPORT_LOC);
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{}
	}
}