package services.community;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.data.htm.HtmCache;
import org.mmocore.gameserver.data.xml.holder.ItemHolder;
import org.mmocore.gameserver.handler.bbs.CommunityBoardHandler;
import org.mmocore.gameserver.handler.bbs.ICommunityBoardHandler;
import org.mmocore.gameserver.instancemanager.AwakingManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.SubClass;
import org.mmocore.gameserver.model.base.ClassId;
import org.mmocore.gameserver.model.base.ClassLevel;
import org.mmocore.gameserver.model.base.SubClassType;
import org.mmocore.gameserver.network.serverpackets.ExSubjobInfo;
import org.mmocore.gameserver.network.serverpackets.ShowBoard;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.scripts.ScriptFile;
import org.mmocore.gameserver.templates.item.ItemTemplate;
import org.mmocore.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy & ALF
 * Community Board v1.0 Career MOD
 * Working if allowed default class master (see config)
 */
public class CareerManager implements ScriptFile, ICommunityBoardHandler
{
	private static final Logger _log = LoggerFactory.getLogger(CareerManager.class);

	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.BBS_PVP_CB_ENABLED)
		{
			_log.info("CommunityBoard: Manage Career service loaded.");
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.BBS_PVP_CB_ENABLED)
			CommunityBoardHandler.getInstance().removeHandler(this);
	}

	@Override
	public void onShutdown()
	{}
	
	@Override
	public String[] getBypassCommands()
	{
		return new String[] { "_bbscareer;", "_bbsclass_change", "_bbsclass_upgrade" };
	}

	@Override
	public void onBypassCommand(Player activeChar, String command)
	{
		if (!activeChar.checkAllowAction())
			return;

		if (command.startsWith("_bbscareer;"))
		{
			String html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/career.htm", activeChar);
			html = html.replace("%classmaster%", String.valueOf(makeMessage(activeChar)));
			ShowBoard.separateAndSend(html, activeChar);
		}
		else if (command.startsWith("_bbsclass_change"))
		{
			String[] args = command.split(" ", -1);

			int val = Integer.parseInt(args[1]);
			long price = Long.parseLong(args[2]);
			if (activeChar.getInventory().destroyItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM, price))
				changeClass(activeChar, val);
			else if (Config.CLASS_MASTERS_PRICE_ITEM == 57)
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
		else if (command.startsWith("_bbsclass_upgrade"))
		{
			if (activeChar.getLevel() < 80)
			{
				activeChar.sendMessage("Вы еще слишком слабы! Приходите как получите 80 уровень!");
				return;
			}

			if (activeChar.getActiveSubClass().isBase())
			{
				activeChar.sendMessage("Вы должны быть на саб-классе");
				return;
			}

			for (SubClass s : activeChar.getSubClassList().values())
				if (s.isDouble())
				{
					activeChar.sendMessage("Вы уже имеете дуал-класс!");
					return;
				}

			activeChar.getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);

			AwakingManager.getInstance().onPlayerEnter(activeChar);

			activeChar.sendMessage("Поздравляем! Вы получили Дуал-Класс");
			activeChar.sendPacket(new ExSubjobInfo(activeChar, true));
		}

	}

	private String makeMessage(Player player)
	{
		ClassId classId = player.getClassId();

		int jobLevel = player.getClassLevel();
		int level = player.getLevel();

		StringBuilder html = new StringBuilder();
		html.append("<br>");
		html.append("<table width=600>");
		html.append("<tr><td>");

		if (Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
			jobLevel = 4;
		if (level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3 && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM);
			html.append("Вы должны заплатить: <font color=\"LEVEL\">");
			html.append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]) + "</font> <font color=\"LEVEL\">" + item.getName() + "</font> для смены профессии<br>");
			html.append("<center><table width=600><tr>");
			for (ClassId cid : ClassId.values())
			{
				if (cid == ClassId.INSPECTOR)
					continue;
				if (cid.childOf(classId) && cid.getClassLevel().ordinal() == classId.getClassLevel().ordinal() + 1)
					html.append("<td><center><button value=\"" + cid.name() + "\" action=\"bypass -h _bbsclass_change " + cid.getId() + " " + Config.CLASS_MASTERS_PRICE_LIST[jobLevel]
							+ "\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>");
			}
			html.append("</tr>");
			html.append("<tr><td><center><button value=\"Дуал-Класс\" action=\"bypass -h _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td></tr>");
			html.append("</table></center>");
			html.append("</td>");
			html.append("</tr>");
			html.append("</table>");
		}
		else
			switch (jobLevel)
			{
				case 1:
					html.append("<center><button value=\"Дуал-Класс\" action=\"bypass -h _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>20-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня.</font><br>");
					break;
				case 2:
					html.append("<center><button value=\"Дуал-Класс\" action=\"bypass -h _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>40-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня.</font><br>");
					break;
				case 3:
					html.append("<center><button value=\"Дуал-Класс\" action=\"bypass -h _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>76-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня</font><br>");
					break;
				case 4:
					html.append("<center><button value=\"Дуал-Класс\" action=\"bypass -h _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для вас больше нет доступных профессий, либо Класс-мастер в данный момент не доступен.<br>");
					if (level < 76)
						break;
					html.append("Вы достигли <font color=F2C202>76-го уровня</font>, активация сабклассов теперь доступна.<br>");
					if (!player.isNoble())
						html.append("Вы можете получить дворянство. Посетите раздел 'Магазин'.<br>");
					else
						html.append("Вы уже дворянин. Получение дворянства более не доступно.<br>");
					break;
			}
		return html.toString();
	}

	private void changeClass(Player player, int val)
	{
		if (player.getClassId().isOfLevel(ClassLevel.Second))
			player.sendPacket(Msg.YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS); // для 3 профы
		else
			player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS); // для 1 и 2 профы

		player.setClassId(val, false, false);
		player.broadcastCharInfo();
	}
	
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
}