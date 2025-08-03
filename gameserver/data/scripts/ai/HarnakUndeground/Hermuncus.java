package ai.HarnakUndeground;

import org.mmocore.gameserver.ai.DefaultAI;
import org.mmocore.gameserver.instancemanager.ReflectionManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.ExStartScenePlayer;

public class Hermuncus extends DefaultAI
{
	private final boolean LAST_SPAWN;

	public Hermuncus(NpcInstance actor)
	{
		super(actor);
		LAST_SPAWN = actor.getParameter("lastSpawn", false);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if(!LAST_SPAWN)
			getActor().setNpcState(1);
	}

	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
		if(ask == 10338 && reply == 2)
		{
			player.teleToLocation(-114962, 226564, -2864, ReflectionManager.DEFAULT);
			player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_VIEW);
		}
	}
}
