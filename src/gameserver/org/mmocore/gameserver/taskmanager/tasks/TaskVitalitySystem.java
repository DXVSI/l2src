package org.mmocore.gameserver.taskmanager.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import org.mmocore.commons.dbutils.DbUtils;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.database.DatabaseFactory;
import org.mmocore.gameserver.model.GameObjectsStorage;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.network.serverpackets.ExVitalityPointInfo;
import org.mmocore.gameserver.taskmanager.Task;
import org.mmocore.gameserver.taskmanager.TaskManager;
import org.mmocore.gameserver.taskmanager.TaskManager.ExecutedTask;
import org.mmocore.gameserver.taskmanager.TaskTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskVitalitySystem extends Task
{
	private static final Logger _log = LoggerFactory.getLogger(TaskVitalitySystem.class);
	private static final String NAME = "sp_vitalitysystem";

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
		{
			_log.info("Vitality System Global Task: launched.");
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.getVitality().setMaximumPoints();
				player.sendPacket(new ExVitalityPointInfo(player.getVitality().getPoints()));
			}

			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE `vitality_points` SET `points`=?");
				statement.setInt(1, Config.MAX_VITALITY);
				statement.execute();
			}
			catch (SQLException e)
			{
				_log.warn("TaskVitalitySystem: error execute sql." + e.getMessage());
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}

			_log.info("Vitality System Task: completed.");
		}
	}

	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}
