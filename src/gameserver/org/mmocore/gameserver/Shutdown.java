package org.mmocore.gameserver;

import org.mmocore.commons.net.nio.impl.SelectorThread;
import org.mmocore.commons.time.cron.SchedulingPattern;
import org.mmocore.commons.time.cron.SchedulingPattern.InvalidPatternException;
import org.mmocore.gameserver.database.DatabaseFactory;
import org.mmocore.gameserver.instancemanager.CoupleManager;
import org.mmocore.gameserver.instancemanager.CursedWeaponsManager;
import org.mmocore.gameserver.instancemanager.games.FishingChampionShipManager;
import org.mmocore.gameserver.loginservercon.LoginServerCommunication;
import org.mmocore.gameserver.model.GameObjectsStorage;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.Hero;
import org.mmocore.gameserver.model.entity.olympiad.OlympiadDatabase;
import org.mmocore.gameserver.network.GameClient;
import org.mmocore.gameserver.network.serverpackets.SystemMessage;
import org.mmocore.gameserver.scripts.Scripts;
import org.mmocore.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс отвечает за запланированное отключение сервера и связанные с этим мероприятия.
 * 
 * @author G1ta0
 */
public class Shutdown extends Thread
{
	public static final int SHUTDOWN = 0;
	public static final int RESTART = 2;
	public static final int NONE = -1;
	private static final Logger _log = LoggerFactory.getLogger(Shutdown.class);
	private static final Shutdown _instance = new Shutdown();
	private Timer counter;
	private int shutdownMode;
	private int shutdownCounter;

	private Shutdown()
	{
		setName(getClass().getSimpleName());
		setDaemon(true);

		shutdownMode = NONE;
	}

	public static final Shutdown getInstance()
	{
		return _instance;
	}

	/**
	 * Время в секундах до отключения.
	 * 
	 * @return время в секундах до отключения сервера, -1 если отключение не запланировано
	 */
	public int getSeconds()
	{
		return shutdownMode == NONE ? -1 : shutdownCounter;
	}

	/**
	 * Режим отключения.
	 * 
	 * @return <code>SHUTDOWN</code> или <code>RESTART</code>, либо <code>NONE</code>, если отключение не запланировано.
	 */
	public int getMode()
	{
		return shutdownMode;
	}

	/**
	 * Запланировать отключение сервера через определенный промежуток времени.
	 * 
	 * @param seconds
	 *            время в секундах
	 * @param shutdownMode
	 *            <code>SHUTDOWN</code> или <code>RESTART</code>
	 */
	public synchronized void schedule(int seconds, int shutdownMode)
	{
		if (seconds < 0)
			return;

		if (counter != null)
			counter.cancel();

		this.shutdownMode = shutdownMode;
		shutdownCounter = seconds;

		_log.info("Scheduled server " + (shutdownMode == SHUTDOWN ? "shutdown" : "restart") + " in " + Util.formatTime(seconds) + ".");

		counter = new Timer("ShutdownCounter", true);
		counter.scheduleAtFixedRate(new ShutdownCounter(), 0, 1000L);
	}

	/**
	 * Запланировать отключение сервера на определенное время.
	 * 
	 * @param time
	 *            время в формате cron
	 * @param shutdownMode
	 *            <code>SHUTDOWN</code> или <code>RESTART</code>
	 */
	public void schedule(String time, int shutdownMode)
	{
		SchedulingPattern cronTime;
		try
		{
			cronTime = new SchedulingPattern(time);
		}
		catch (InvalidPatternException e)
		{
			return;
		}

		int seconds = (int) (cronTime.next(System.currentTimeMillis()) / 1000L - System.currentTimeMillis() / 1000L);
		schedule(seconds, shutdownMode);
	}

	/**
	 * Отменить запланированное отключение сервера.
	 */
	public synchronized void cancel()
	{
		shutdownMode = NONE;
		if (counter != null)
			counter.cancel();
		counter = null;
	}

	@Override
	public void run()
	{
		System.out.println("Shutting down LS/GS communication...");
		LoginServerCommunication.getInstance().shutdown();

		System.out.println("Shutting down scripts...");
		Scripts.getInstance().shutdown();

		System.out.println("Disconnecting players...");
		disconnectAllPlayers();

		System.out.println("Saving data...");
		saveData();

		try
		{
			System.out.println("Shutting down thread pool...");
			ThreadPoolManager.getInstance().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("Shutting down selector...");
		if (GameServer.getInstance() != null)
			for (SelectorThread<GameClient> st : GameServer.getInstance().getSelectorThreads())
				try
				{
					st.shutdown();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

		try
		{
			System.out.println("Shutting down database communication...");
			DatabaseFactory.getInstance().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("Shutdown finished.");
	}

	private void saveData()
	{

		if (Config.ENABLE_OLYMPIAD)
			try
			{
				OlympiadDatabase.save();
				System.out.println("Olympiad: Data saved.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		if (Config.ALLOW_WEDDING)
			try
			{
				CoupleManager.getInstance().store();
				System.out.println("CoupleManager: Data saved.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		try
		{
			FishingChampionShipManager.getInstance().shutdown();
			System.out.println("FishingChampionShipManager: Data saved.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			Hero.getInstance().shutdown();
			System.out.println("Hero: Data saved.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (Config.ALLOW_CURSED_WEAPONS)
			try
			{
				CursedWeaponsManager.getInstance().saveData();
				System.out.println("CursedWeaponsManager: Data saved,");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}

	private void disconnectAllPlayers()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			try
			{
				player.logout();
			}
			catch (Exception e)
			{
				System.out.println("Error while disconnecting: " + player + "!");
				e.printStackTrace();
			}
	}

	private class ShutdownCounter extends TimerTask
	{
		@Override
		public void run()
		{
			switch (shutdownCounter)
			{
				case 1800:
				case 900:
				case 600:
				case 300:
				case 240:
				case 180:
				case 120:
				case 60:
					Announcements.getInstance().announceByCustomMessage("THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_MINUTES", new String[] { String.valueOf(shutdownCounter / 60) });
					break;
				case 30:
				case 20:
				case 10:
				case 5:
					Announcements.getInstance().announceToAll(
					        new SystemMessage(SystemMessage.THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS__PLEASE_FIND_A_SAFE_PLACE_TO_LOG_OUT).addNumber(shutdownCounter));
					break;
				case 0:
					switch (shutdownMode)
					{
						case SHUTDOWN:
							Runtime.getRuntime().exit(SHUTDOWN);
							break;
						case RESTART:
							Runtime.getRuntime().exit(RESTART);
							break;
					}
					cancel();
					return;
			}

			shutdownCounter--;
		}
	}
}