package org.mmocore.gameserver.dao;

import org.mmocore.commons.dbutils.DbUtils;
import org.mmocore.gameserver.database.DatabaseFactory;
import org.mmocore.gameserver.model.PremiumItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Дмитрий
 * @date 06.11.12  22:20
 */
public class CharactersPremiumItemsDAO {
	private static final Logger log = LoggerFactory.getLogger(CharactersPremiumItemsDAO.class);
	private static final String SELECT_QUERY = "SELECT itemNum, itemId, itemCount, itemSender FROM character_premium_items WHERE charId=?";
	private static final String UPDATE_QUERY = "UPDATE character_premium_items SET itemCount=? WHERE charId=? AND itemNum=?";
	private static final String DELETE_QUERY = "DELETE FROM character_premium_items WHERE charId=? AND itemNum=?";
	private static CharactersPremiumItemsDAO ourInstance = new CharactersPremiumItemsDAO();

	private CharactersPremiumItemsDAO() {
	}

	public static CharactersPremiumItemsDAO getInstance() {
		return ourInstance;
	}

	public Map<Integer, PremiumItem> loadPremiumItemList(int playerObjId) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<Integer, PremiumItem> premiumItemMap = new HashMap<>();
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, playerObjId);
			rs = statement.executeQuery();
			while (rs.next()) {
				int itemNum = rs.getInt("itemNum");
				int itemId = rs.getInt("itemId");
				long itemCount = rs.getLong("itemCount");
				String itemSender = rs.getString("itemSender");
				PremiumItem item = new PremiumItem(itemId, itemCount, itemSender);
				premiumItemMap.put(itemNum, item);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			DbUtils.closeQuietly(con, statement, rs);
		}
		return premiumItemMap;
	}

	public void updatePremiumItem(int playerObjId, int itemNum, long newcount) {
		Connection con = null;
		PreparedStatement statement = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_QUERY);
			statement.setLong(1, newcount);
			statement.setInt(2, playerObjId);
			statement.setInt(3, itemNum);
			statement.execute();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deletePremiumItem(int playerObjId, int itemNum) {
		Connection con = null;
		PreparedStatement statement = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, playerObjId);
			statement.setInt(2, itemNum);
			statement.execute();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			DbUtils.closeQuietly(con, statement);
		}
	}
}
