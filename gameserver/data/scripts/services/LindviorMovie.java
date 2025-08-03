package services;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.network.serverpackets.ExStartScenePlayer;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * Раз в 3 часа на всей территории базы альянса на Грации всем внутри зоны показывается мувик
 *
 * @author pchayka
 */

public class LindviorMovie implements ScriptFile
{
	private static long movieDelay = 3 * 60 * 60 * 1000L; // показывать мувик раз в n часов

	@Override
	public void onLoad()
	{
		Zone zone = ReflectionUtils.getZone("[keucereus_alliance_base_town_peace]");
		zone.setActive(true);

		ThreadPoolManager.getInstance().scheduleAtFixedRate(new ShowLindviorMovie(zone), movieDelay, movieDelay);
	}

	public class ShowLindviorMovie extends RunnableImpl
	{
		Zone _zone;

		public ShowLindviorMovie(Zone zone)
		{
			_zone = zone;
		}

		@Override
		public void runImpl() throws Exception
		{
			GArray<Player> insideZoners = _zone.getInsidePlayers();

			if(insideZoners != null && !insideZoners.isEmpty())
				for(Player player : insideZoners)
					if(!player.isInBoat() && !player.isInFlyingTransform())
						player.showQuestMovie(ExStartScenePlayer.SCENE_LINDVIOR);
		}
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
