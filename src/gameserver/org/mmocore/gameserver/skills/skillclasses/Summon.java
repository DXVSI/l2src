package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.dao.EffectsDAO;
import org.mmocore.gameserver.data.xml.holder.NpcHolder;
import org.mmocore.gameserver.idfactory.IdFactory;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.FakePlayer;
import org.mmocore.gameserver.model.GameObjectTasks;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.base.Experience;
import org.mmocore.gameserver.model.entity.events.impl.SiegeEvent;
import org.mmocore.gameserver.model.instances.MerchantInstance;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.instances.SummonInstance;
import org.mmocore.gameserver.model.instances.TrapInstance;
import org.mmocore.gameserver.model.instances.TreeInstance;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.stats.Stats;
import org.mmocore.gameserver.stats.funcs.FuncAdd;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.StatsSet;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;

public class Summon extends Skill
{
	private final SummonType _summonType;

	private final double _expPenalty;
	private final int _itemConsumeIdInTime;
	private final int _itemConsumeCountInTime;
	private final int _itemConsumeDelay;
	private final int _lifeTime;

	private static enum SummonType
	{
		PET, SIEGE_SUMMON, AGATHION, TRAP, TREE, CLONE, MERCHANT
	}

	public Summon(StatsSet set)
	{
		super(set);

		_summonType = Enum.valueOf(SummonType.class, set.getString("summonType", "PET").toUpperCase());
		_expPenalty = set.getDouble("expPenalty", 0.f);
		_itemConsumeIdInTime = set.getInteger("itemConsumeIdInTime", 0);
		_itemConsumeCountInTime = set.getInteger("itemConsumeCountInTime", 0);
		_itemConsumeDelay = set.getInteger("itemConsumeDelay", 240) * 1000;
		_lifeTime = set.getInteger("lifeTime", 1200) * 1000;
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = activeChar.getPlayer();
		if (player == null)
			return false;
		if (player.isProcessingRequest())
		{
			player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}

		switch (_summonType)
		{
			case TRAP:
				if (player.isInZonePeace())
				{
					activeChar.sendPacket(Msg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
					return false;
				}
				break;
			case CLONE:
				// TODO: Спавн клона чара
				break;
			case TREE:
				if (player.getTree())
				{
					player.sendMessage("You already have a tree.");
					return false;
				}
				break;
			case PET:
			{
				if (getNpcId() == 0)
				{
					System.out.println("[WARNING]: Error in skill id:" + getId());
					return false;
				}
				NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				if (summonTemplate == null)
				{
					System.out.println("[WARNING]: Error in skill id:" + getId());
					return false;
				}
				if (!player.canSummon(getNpcId()) || player.isMounted())
				{
					player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
					return false;
				}
				break;
			}
			case SIEGE_SUMMON:
				if (player.getFirstPet() == null && !player.isMounted())
					break;
				player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
				return false;
			case AGATHION:
				if (player.getAgathionId() <= 0 || _npcId == 0)
					break;
				player.sendPacket(SystemMsg.AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED);
				return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature caster, GArray<Creature> targets)
	{
		Player activeChar = caster.getPlayer();

		switch (_summonType)
		{
			case AGATHION:
				activeChar.setAgathion(getNpcId());
				break;
			case TRAP:
				Skill trapSkill = getFirstAddedSkill();

				if (activeChar.getTrapsCount() >= 5)
					activeChar.destroyFirstTrap();
				TrapInstance trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, trapSkill);
				activeChar.addTrap(trap);
				trap.spawnMe();
				break;
			case CLONE:
				FakePlayer fp = new FakePlayer(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar);
				fp.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
				fp = new FakePlayer(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar);
				fp.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
				break;
			case TREE:
				if (activeChar.getTree())
				{
					activeChar.sendMessage("You already have a tree.");
					return;
				}
				Skill treeSkill = getFirstAddedSkill();
				TreeInstance tree = new TreeInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, _lifeTime, treeSkill);
				activeChar.setTree(true);
				tree.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
				break;
			case PET:
			case SIEGE_SUMMON:
				// Удаление трупа, если идет суммон из трупа.
				Location loc = null;
				if (_targetType == Skill.SkillTargetType.TARGET_CORPSE)
					for (Creature target : targets)
						if (target != null && target.isDead())
						{
							activeChar.getAI().setAttackTarget(null);
							loc = target.getLoc();
							if (target.isNpc())
								((NpcInstance) target).endDecayTask();
							else if (target.isSummon())
								((SummonInstance) target).endDecayTask();
							else
								return;
						}
				NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				if (!activeChar.canSummon(getNpcId()) || activeChar.isMounted())
				{
					activeChar.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
					return;
				}

				SummonInstance summon = new SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, activeChar, _lifeTime, _itemConsumeIdInTime, _itemConsumeCountInTime,
				        _itemConsumeDelay, this);
				activeChar.addPet(summon);

				summon.setTitle(activeChar.getName());
				summon.setExpPenalty(_expPenalty);
				summon.setExp(Experience.LEVEL[java.lang.Math.min(summon.getLevel(), Experience.LEVEL.length - 1)]);
				summon.setHeading(activeChar.getHeading());
				summon.setReflection(activeChar.getReflection());
				summon.spawnMe(loc == null ? Location.findAroundPosition(activeChar, 50, 70) : loc);
				summon.setRunning();
				summon.setFollowMode(true);

				if (summon.getSkillLevel(Integer.valueOf(4140)) > 0)
					summon.altUseSkill(SkillTable.getInstance().getInfo(4140, summon.getSkillLevel(Integer.valueOf(4140))), activeChar);
				if (summon.getName().equalsIgnoreCase("Shadow"))
					summon.addStatFunc(new FuncAdd(Stats.ABSORB_DAMAGE_PERCENT, 64, this, 15.0D));
				EffectsDAO.getInstance().restoreEffects(summon);
				if (activeChar.isInOlympiadMode())
					summon.getEffectList().stopAllEffects();
				summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp(), false);

				if (_summonType != SummonType.SIEGE_SUMMON)
					break;
				SiegeEvent siegeEvent = activeChar.getEvent(SiegeEvent.class);

				siegeEvent.addSiegeSummon(summon);
				break;
			case MERCHANT:
				if (activeChar.getFirstPet() != null || activeChar.isMounted())
					return;

				NpcTemplate merchantTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				MerchantInstance merchant = new MerchantInstance(IdFactory.getInstance().getNextId(), merchantTemplate);

				merchant.setCurrentHp(merchant.getMaxHp(), false);
				merchant.setCurrentMp(merchant.getMaxMp());
				merchant.setHeading(activeChar.getHeading());
				merchant.setReflection(activeChar.getReflection());
				merchant.spawnMe(activeChar.getLoc());

				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(merchant), _lifeTime);
				break;
		}

		if (isSSPossible())
			caster.unChargeShots(isMagic());
	}

	@Override
	public boolean isOffensive()
	{
		return _targetType == SkillTargetType.TARGET_CORPSE;
	}
}