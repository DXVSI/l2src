package org.mmocore.gameserver.instancemanager;

import gnu.trove.map.hash.TIntIntHashMap;

import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.listener.actor.player.OnPlayerEnterListener;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.actor.listener.CharListenerList;
import org.mmocore.gameserver.model.base.ClassLevel;
import org.mmocore.gameserver.network.serverpackets.ExCallToChangeClass;
import org.mmocore.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import org.mmocore.gameserver.network.serverpackets.ExShowUsmVideo;
import org.mmocore.gameserver.network.serverpackets.SocialAction;
import org.mmocore.gameserver.tables.SkillTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ALF
 * @data 04.02.2012
 */
public class AwakingManager implements OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
	private static AwakingManager _instance;

	private static TIntIntHashMap _CA = new TIntIntHashMap(36);

	/**
	 * ************************************************************************* ***************** 139 Рыцарь Савуло: Рыцарь Феникса, Рыцарь Ада,
	 * Храмовник Евы, Храмовник Шилен. 140 Воин Тейваза: Полководец, Дуэлист, Титан, Аватар, Мастер, Каратель. 141 Разбойник Отилы: Авантюрист,
	 * Странник Ветра, Призрачный Охотник, Кладоискатель. 142 Лучник Эйваза: Снайпер, Страж Лунного Света, Страж Теней, Диверсант. 143 Волшебник Фео:
	 * Archmahe, Soultaker, Mystic muse, StormScreamer, SoulHound 144 Ис Заклинатель: Hierophant, Doomcryer, Dominator, Sword Muse, Spectral Dancer,
	 * Judicator 145 Призыватель Веньо: Arcana Lord, Elemental master, Spectral Master 146 Целитель Альгиза: Cardinal, Eva’s Saint, Shilien saint
	 * ******************************************************************** ************************
	 */

	public void load()
	{

		if (Config.AWAKING_FREE)
			CharListenerList.addGlobal(this);

		_CA.clear();

		_CA.put(90, 139);
		_CA.put(91, 139);
		_CA.put(99, 139);
		_CA.put(106, 139);
		_CA.put(89, 140);
		_CA.put(88, 140);
		_CA.put(113, 140);
		_CA.put(114, 140);
		_CA.put(118, 140);
		_CA.put(131, 140);
		_CA.put(93, 141);
		_CA.put(101, 141);
		_CA.put(108, 141);
		_CA.put(117, 141);
		_CA.put(92, 142);
		_CA.put(102, 142);
		_CA.put(109, 142);
		_CA.put(134, 142);
		_CA.put(94, 143);
		_CA.put(95, 143);
		_CA.put(103, 143);
		_CA.put(110, 143);
		_CA.put(132, 143);
		_CA.put(133, 143);
		_CA.put(98, 144);
		_CA.put(116, 144);
		_CA.put(115, 144);
		_CA.put(100, 144);
		_CA.put(107, 144);
		_CA.put(136, 144);
		_CA.put(96, 145);
		_CA.put(104, 145);
		_CA.put(111, 145);
		_CA.put(97, 146);
		_CA.put(105, 146);
		_CA.put(112, 146);

		_log.info("AwakingManager: Loaded 8 Awaking class for " + _CA.size() + " normal class.");

	}

	public static AwakingManager getInstance()
	{
		if (_instance == null)
		{
			_log.info("Initializing: AwakingManager");
			_instance = new AwakingManager();
			_instance.load();
		}
		return _instance;
	}

	// TODO: Проверки для начала квеста...
	public void SendReqToStartQuest(Player player)
	{
		if (!player.getClassId().isOfLevel(ClassLevel.Fourth))
			return;

		int newClass = _CA.get(player.getClassId().getId());

		player.sendPacket(new ExCallToChangeClass(newClass, false));

	}

	// TODO: Проверки для пробуждения...
	public void SendReqToAwaking(Player player)
	{
		if (!player.getClassId().isOfLevel(ClassLevel.Fourth))
			return;
		int newClass = _CA.get(player.getClassId().getId());
		player.sendPacket(new ExChangeToAwakenedClass(newClass));
		return;
	}

	public void onStartQuestAccept(Player player)
	{
		// Телепортируем в музей
		player.teleToLocation(-114708, 243918, -7968);
		// Показываем видео
		player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
		return;
	}

	public void SetAwakingId(Player player)
	{
		int _oldId = player.getClassId().getId();
		player.setClassId(_CA.get(_oldId), false, false);
		player.broadcastUserInfo(true);
		player.sendUserInfo();
		player.updateStats();
		player.broadcastPacket(new SocialAction(player.getObjectId(), (_CA.get(_oldId) - 119)));

	}

	@Override
	public void onPlayerEnter(Player player)
	{

		if (!player.getClassId().isOfLevel(ClassLevel.Fourth))
			return;
		if (player.getLevel() < 85)
			return;
		if (player.isAwaking())
			return;
		if (player.getActiveSubClass().isBase() || player.getActiveSubClass().isDouble())
		{
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
			player.sendPacket(new ExCallToChangeClass(_CA.get(player.getClassId().getId()), true));
		}

	}
	
	public Skill getRaceSkill(Player player)
	{
		int race = player.getRace().ordinal();

		Skill skill = null;
		if (player.getClassId().isOfLevel(ClassLevel.Awaking))
		{
			switch (race)
			{
				case 0:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 1:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 2:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 3:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 4:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 5:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
			}
		}
		else
		{
			player.sendActionFailed();
		}
		player.updateStats();
		return null;
	}
}
