package org.mmocore.gameserver.taskmanager;

import java.util.concurrent.Future;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.commons.threading.SteppingRunnableQueueManager;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Spawner;
import org.mmocore.gameserver.model.instances.NpcInstance;

/**
 * @author ALF
 * @date 22.08.2012 Шаг выполенния задач 1000 мс.
 */
public class SpawnTaskManager extends SteppingRunnableQueueManager
{
	private static final SpawnTaskManager _instance = new SpawnTaskManager();

	public static final SpawnTaskManager getInstance()
	{
		return _instance;
	}

	private SpawnTaskManager()
	{
		super(1000L);

		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000L, 1000L);

		// Очистка каждую минуту
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl() throws Exception
			{
				SpawnTaskManager.this.purge();
			}

		}, 60000L, 60000L);
	}

	public Future<?> addSpawnTask(final NpcInstance actor, long interval)
	{
		return schedule(new RunnableImpl()
		{

			@Override
			public void runImpl() throws Exception
			{
				if (actor == null)
					return;

				if (actor.getSpawn() == null)
					return;

				Spawner spawn = actor.getSpawn();
				spawn.decreaseScheduledCount();
				if (spawn.isDoRespawn())
					spawn.respawnNpc(actor);
			}

		}, 60000);
	}
}