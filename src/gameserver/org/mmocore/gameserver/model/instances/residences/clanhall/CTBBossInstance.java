package org.mmocore.gameserver.model.instances.residences.clanhall;

import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.entity.events.impl.ClanHallTeamBattleEvent;
import org.mmocore.gameserver.model.entity.events.objects.CTBSiegeClanObject;
import org.mmocore.gameserver.model.entity.events.objects.CTBTeamObject;
import org.mmocore.gameserver.model.instances.MonsterInstance;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.apache.commons.lang3.StringUtils;

/**
 * @author VISTALL
 * @date 16:55/21.04.2011
 */
public abstract class CTBBossInstance extends MonsterInstance
{
	/**
     *
     */
	private static final long serialVersionUID = -6817837878001156938L;
	public static final Skill SKILL = SkillTable.getInstance().getInfo(5456, 1);
	private CTBTeamObject _matchTeamObject;

	public CTBBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}

	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage,
	        boolean isDot, boolean sendMessage)
	{
		if (attacker.getLevel() > getLevel() + 8 && attacker.getEffectList().getEffectsCountForSkill(SKILL.getId()) == 0)
		{
			doCast(SKILL, attacker, false);
			return;
		}

		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		CTBSiegeClanObject clan = _matchTeamObject.getSiegeClan();
		if (clan != null && attacker.isPlayable())
		{
			Player player = attacker.getPlayer();
			if (player.getClan() == clan.getClan())
				return false;
		}
		return true;
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return isAttackable(attacker);
	}

	@Override
	public void onDeath(Creature killer)
	{
		ClanHallTeamBattleEvent event = getEvent(ClanHallTeamBattleEvent.class);
		event.processStep(_matchTeamObject);

		super.onDeath(killer);
	}

	@Override
	public String getTitle()
	{
		CTBSiegeClanObject clan = _matchTeamObject.getSiegeClan();
		return clan == null ? StringUtils.EMPTY : clan.getClan().getName();
	}

	public void setMatchTeamObject(CTBTeamObject matchTeamObject)
	{
		_matchTeamObject = matchTeamObject;
	}
}
