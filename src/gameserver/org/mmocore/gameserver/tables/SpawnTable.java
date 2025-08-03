package org.mmocore.gameserver.tables;

import org.mmocore.commons.dbutils.DbUtils;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.data.xml.holder.SpawnHolder;
import org.mmocore.gameserver.database.DatabaseFactory;
import org.mmocore.gameserver.model.SimpleSpawner;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.templates.spawn.PeriodOfDay;
import org.mmocore.gameserver.templates.spawn.SpawnNpcInfo;
import org.mmocore.gameserver.templates.spawn.SpawnTemplate;
import org.mmocore.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ALF
 * @data 07.02.2012 Это "Простой спавн" Использовать для спавна НПЦ и простых мобов
 */
public class SpawnTable
{
	private static final Logger _log = LoggerFactory.getLogger(SpawnTable.class);

	private static SpawnTable _instance;

	public static SpawnTable getInstance()
	{
		if (_instance == null)
			new SpawnTable();
		return _instance;
	}

	private SpawnTable()
	{
		_instance = this;
		if (!Config.DONTLOADSPAWN)
			fillSpawnTable();
		if (Config.LOAD_CUSTOM_SPAWN)
			fillCustomSpawnTable();
	}

	private void fillSpawnTable()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM spawnlist ORDER by npc_templateid");
			rset = statement.executeQuery();
			SpawnTemplate template;
			while (rset.next())
			{
				int count = rset.getInt("count");
				int delay = rset.getInt("respawn_delay");
				int delay_rnd = rset.getInt("respawn_delay_rnd");
				int npcId = rset.getInt("npc_templateid");
				int x = rset.getInt("locx");
				int y = rset.getInt("locy");
				int z = rset.getInt("locz");
				int h = rset.getInt("heading");

				template = new SpawnTemplate(PeriodOfDay.NONE, count, delay, delay_rnd);
				template.addNpc(new SpawnNpcInfo(npcId, 1, StatsSet.EMPTY));
				template.addSpawnRange(new Location(x, y, z, h));
				SpawnHolder.getInstance().addSpawn(PeriodOfDay.NONE.name(), template);
			}
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be initialized:" + e1);
			e1.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	private void fillCustomSpawnTable()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM custom_spawnlist ORDER by npc_templateid");
			rset = statement.executeQuery();
			SpawnTemplate template;
			while (rset.next())
			{
				int count = rset.getInt("count");
				int delay = rset.getInt("respawn_delay");
				int delay_rnd = rset.getInt("respawn_delay_rnd");
				int npcId = rset.getInt("npc_templateid");
				int x = rset.getInt("locx");
				int y = rset.getInt("locy");
				int z = rset.getInt("locz");
				int h = rset.getInt("heading");

				template = new SpawnTemplate(PeriodOfDay.NONE, count, delay, delay_rnd);
				template.addNpc(new SpawnNpcInfo(npcId, 1, StatsSet.EMPTY));
				template.addSpawnRange(new Location(x, y, z, h));
				SpawnHolder.getInstance().addSpawn(PeriodOfDay.NONE.name(), template);
			}
		}
		catch (Exception e1)
		{
			_log.warn("custom_spawnlist couldnt be initialized:" + e1);
			e1.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void addNewSpawn(SimpleSpawner spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO `custom_spawnlist` (location,count,npc_templateid,locx,locy,locz,heading,respawn_delay) values(?,?,?,?,?,?,?,?)");
			statement.setString(1, "");
			statement.setInt(2, spawn.getAmount());
			statement.setInt(3, spawn.getCurrentNpcId());
			statement.setInt(4, spawn.getLocx());
			statement.setInt(5, spawn.getLocy());
			statement.setInt(6, spawn.getLocz());
			statement.setInt(7, spawn.getHeading());
			statement.setInt(8, spawn.getRespawnDelay());
			statement.execute();
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be stored in db:" + e1);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deleteSpawn(SimpleSpawner spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM custom_spawnlist WHERE locx=? AND locy=? AND locz=? AND npc_templateid=? AND heading=?");
			statement.setInt(1, spawn.getLocx());
			statement.setInt(2, spawn.getLocy());
			statement.setInt(3, spawn.getLocz());
			statement.setInt(4, spawn.getCurrentNpcId());
			statement.setInt(5, spawn.getHeading());
			statement.execute();
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be deleted in db:" + e1);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

}
