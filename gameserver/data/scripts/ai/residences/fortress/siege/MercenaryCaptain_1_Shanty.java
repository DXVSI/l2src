package ai.residences.fortress.siege;

import java.util.List;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ai.CtrlIntention;
import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.entity.events.impl.FortressSiegeEvent;
import org.mmocore.gameserver.model.entity.events.objects.DoorObject;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.pledge.Clan;
import org.mmocore.gameserver.utils.Location;

public class MercenaryCaptain_1_Shanty extends Fighter
{

	public MercenaryCaptain_1_Shanty(NpcInstance actor)
	{
		super(actor);
	}

	static final Location[] points = {
			new Location(-49533, 155484, -2112),
			new Location(-49857, 155590, -2064),
			new Location(-50205, 155724, -2056),
			new Location(-50641, 155873, -2056),
			new Location(-51002, 155980, -2048),
			new Location(-51272, 156054, -2048),
			new Location(-51492, 156114, -2048),
			new Location(-51667, 156165, -2048),
			new Location(-51812, 156203, -2048),
			new Location(-51987, 156254, -2048) };

	private int current_point = -1;
	private long wait_timeout = 0;
	private boolean wait = false;

	@Override
	public int getMaxPathfindFails()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE)
			return false;

		super.checkAggression(target);

		// Продолжит идти с предыдущей точки
		if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE && current_point > -1)
			current_point--;

		return true;
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if (actor != null && attacker != null && attacker.getPlayer() != null)
		{
			Clan clan = attacker.getPlayer().getClan();
			// РќРµ Р°РіСЂРёРјСЃСЏ РЅР° РёРіСЂРѕРєРѕРІ РєРѕС‚РѕСЂС‹Рµ Р·Р°РїРёСЃР°РЅС‹ РЅР° РѕСЃР°РґСѓ РєР°Рє РђС‚Р°РєРµСЂС‹
			// TODO:
			// if(clan != null && ResidenceHolder.getInstance().getSiege(actor) == clan.getSiege() && clan.isAttacker())
			// return;
		}
		super.onEvtAttacked(attacker, damage);
	}

	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		NpcInstance actor = getActor();
		Player targetPlayer = target.getPlayer();
		if (actor != null && target != null && targetPlayer != null)
		{
			Clan clan = targetPlayer.getClan();
			// Р•СЃР»Рё РЅР°СЃ Р±СЊС‘С‚ РёРіСЂРѕРє РёР· РђС‚Р°РєРµСЂРѕРІ С‚Рѕ РјС‹ РЅРµ РѕС‚РІРµС‡Р°РµРј РІ РѕР±СЂР°С‚РЅСѓСЋ
			// TODO:
			// if(clan != null && ResidenceHolder.getInstance().getSiege(actor) == clan.getSiege() && clan.isAttacker())
			// return;
		}
		super.onEvtAggression(target, aggro);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;

		if(_globalAggro < 0)
			_globalAggro++;
		else if(_globalAggro > 0)
			_globalAggro--;

		if(_def_think)
		{
			if(doTask())
				clearTasks();
			return true;
		}

		// TODO:
		//actor.setAttackTimeout(Long.MAX_VALUE);

		if(super.thinkActive())
			return true;

		if(System.currentTimeMillis() > wait_timeout && (current_point > -1 || Rnd.chance(5)))
		{
			if(!wait)
				switch(current_point)
				{
					case 4:
						// Перед битвой с дверями делаем паузу на 3 секунды
						wait_timeout = System.currentTimeMillis() + 3000;
						// Смотрим все двери резиденции
						// TODO:
						List<DoorObject> _doors = actor.getFortress().getSiegeEvent().getObjects(FortressSiegeEvent.COMMANDER_DOORS);
						for(DoorObject obj : _doors)
							// Если это входные двери в крепость то атакуем их
							if(obj != null && obj.getDoor().getDoorId() == 18220001 && actor.getDistance(obj.getDoor()) <= 1000)
								setIntention(CtrlIntention.AI_INTENTION_ATTACK, obj);
						// TODO:
						//obj.getDoor().addDamage(actor, 10);
						wait = true;
						return true;
					case 5:
						// Ставим себя в стандартное положение
						if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE)
							setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
						// Вдруг мы бегаем, тогда выключим бег
						if(actor.isRunning())
							actor.setWalking();
						// Кричим в чат о том что мы ворвались в крепость
						//TODO:
						//Functions.npcShout(actor, NpcString.THE_HAVE_BROKEN_THROUGH_THE_GATE_DESTROY_THE_ENCAMPMENT_AND_MOVE_TO_THE_COMMAND_POST);
						// Мы разрушили двери и нам нужно отдохнуть 3 секунды
						wait_timeout = System.currentTimeMillis() + 3000;
						wait = true;
						return true;
					case 10:
						FortressSiegeEvent siege = actor.getEvent(FortressSiegeEvent.class);

						// Кричим в чат о том что мы открыли ворота к флагам
						// TODO:
						//Functions.npcShout(actor, NpcString.THS_COMMAND_GATE_HAS_OPENED_CAPTURE_THE_FLAG_QUICKLY_AND_RAISE_IT_HITH_TO_PROCLAIM_OUR_VICTORYs);
						// Спавним Флаги
						siege.spawnAction(FortressSiegeEvent.COMBAT_FLAGS, true);
						// Открываем Двери к баракам где находятся Флаги
						siege.doorAction(FortressSiegeEvent.COMMANDER_DOORS, true);

						// Мы дошли до конца и нам нужно отдохнуть 20 секунд
						wait_timeout = System.currentTimeMillis() + 20000;
						wait = true;
						return true;
				}

			wait = false;
			current_point++;

			// Точки кончились и мы нечего не делаем
			if(current_point >= points.length)
				return false;

			// Когда нас никто не трогает то мы просто ходим
			actor.setWalking();

			addTaskMove(points[current_point], true);
			doTask();
			return true;
		}

		if(randomAnimation())
			return false;

		return false;
	}

	@Override
	public void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();

		if(actor == null)
			return;

		FortressSiegeEvent siege = actor.getEvent(FortressSiegeEvent.class);

		if(siege == null)
			return;

		if(siege instanceof FortressSiegeEvent)
		{
			// TODO : По идеи тут надо удалять!!
			// Спавним Флаги
			siege.spawnAction(FortressSiegeEvent.COMBAT_FLAGS, true);
			// Открываем Двери к баракам где находятся Флаги
			siege.doorAction(FortressSiegeEvent.COMMANDER_DOORS, true);
		}

		super.onEvtDead(killer);
	}
}
