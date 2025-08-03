package zones;

import org.mmocore.gameserver.listener.zone.OnZoneEnterLeaveListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.model.quest.QuestState;
import org.mmocore.gameserver.network.serverpackets.components.SceneMovie;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.ReflectionUtils;

import quests._10320_LetsGototheCentralSquare;

/**
 * @author K1mel
 */
public class TalkingIslandMovie implements ScriptFile
{
	private static final String ZONE_NAME = "[ti_presentation_video]";

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
				if(!player.getVarB("@ti_present_video"))
				{
					QuestState qs = player.getQuestState(_10320_LetsGototheCentralSquare.class);
					if(qs != null && qs.getCond() == 1)
						player.showQuestMovie(SceneMovie.si_illusion_02_que);
					else
						player.showQuestMovie(SceneMovie.si_illusion_01_que);
					player.setVar("@ti_present_video", "true", -1);
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{}
	}
}