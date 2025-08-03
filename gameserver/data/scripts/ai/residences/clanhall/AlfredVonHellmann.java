package ai.residences.clanhall;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import org.mmocore.gameserver.model.entity.events.objects.SpawnExObject;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.utils.PositionUtils;
import org.mmocore.gameserver.utils.ReflectionUtils;

import ai.residences.SiegeGuardFighter;

/**
 * @author VISTALL
 * @date 12:21/08.05.2011
 * 35630
 * <p/>
 * При убийстве если верить Аи, то говорит он 1010635, но на aинале он говорит как и  GiselleVonHellmann
 * lidia_zone3
 */
public class AlfredVonHellmann extends SiegeGuardFighter
{
	public static final Skill DAMAGE_SKILL = SkillTable.getInstance().getInfo(5000, 1);
	public static final Skill DRAIN_SKILL = SkillTable.getInstance().getInfo(5001, 1);

	private static Zone ZONE_3 = ReflectionUtils.getZone("lidia_zone3");

	public AlfredVonHellmann(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();

		ZONE_3.setActive(true);

		Functions.npcShout(getActor(), NpcString.HEH_HEH_I_SEE_THAT_THE_FEAST_HAS_BEGAN_BE_WARY_THE_CURSE_OF_THE_HELLMANN_FAMILY_HAS_POISONED_THIS_LAND);
	}

	@Override
	public void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();

		super.onEvtDead(killer);

		ZONE_3.setActive(false);

		Functions.npcShout(actor, NpcString.AARGH_IF_I_DIE_THEN_THE_MAGIC_FORCE_FIELD_OF_BLOOD_WILL);

		ClanHallSiegeEvent siegeEvent = actor.getEvent(ClanHallSiegeEvent.class);
		if(siegeEvent == null)
			return;
		SpawnExObject spawnExObject = siegeEvent.getFirstObject(ClanHallSiegeEvent.BOSS);
		NpcInstance lidiaNpc = spawnExObject.getFirstSpawned();

		if(lidiaNpc.getCurrentHpRatio() == 1.)
			lidiaNpc.setCurrentHp(lidiaNpc.getMaxHp() / 2, true);
	}

	@Override
	public void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();

		super.onEvtAttacked(attacker, damage);

		if(PositionUtils.calculateDistance(attacker, actor, false) > 300. && Rnd.chance(0.13))
			addTaskCast(attacker, DRAIN_SKILL);

		Creature target = actor.getAggroList().getMostHated();
		if(target == attacker && Rnd.chance(0.3))
			addTaskCast(attacker, DAMAGE_SKILL);
	}
}
