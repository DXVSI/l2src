package zones;

import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.network.serverpackets.components.SceneMovie;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.ReflectionUtils;

public class RuinsOfEsagiraMovie implements ScriptFile
{
	private static final String ZONE_NAME = "[roe_presentation_video]";

	private static ZoneListener _zoneListener;

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

	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(ZONE_NAME);
		if(zone != null)
			zone.addListener(_zoneListener);
	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(cha.isPlayer())
			{
				Player player = cha.getPlayer();
				if(!player.getVarB("@roe_present_video"))
				{
					player.showQuestMovie(SceneMovie.si_illusion_03_que);
					player.setVar("@roe_present_video", "true", -1);
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{}
	}
}