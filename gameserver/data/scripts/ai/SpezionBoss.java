package ai;

import java.util.concurrent.ScheduledFuture;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.ai.CtrlEvent;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.commons.threading.RunnableImpl;

/**
 * @author cruel
 */
public class SpezionBoss extends Fighter
{
	private ScheduledFuture<?> DeadTask;
	public SpezionBoss(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		NpcInstance actor = getActor();
		DeadTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnMinion(),1000,30000);
		Reflection r = actor.getReflection();
		for(Player p : r.getPlayers())
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		super.onEvtSpawn();
		Skill fp = SkillTable.getInstance().getInfo(14190, 1);
		fp.getEffects(actor, actor, false, false);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		if(DeadTask != null)
			DeadTask.cancel(true);

		super.onEvtDead(killer);
	}
	
	public class SpawnMinion extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			NpcInstance minion = actor.getReflection().addSpawnWithoutRespawn(25780, actor.getLoc(), 250);
			for(Player p : actor.getReflection().getPlayers())
				minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		}
	}
}
