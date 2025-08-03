package org.mmocore.gameserver.data.xml.holder;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.data.xml.AbstractHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.SkillLearn;
import org.mmocore.gameserver.model.base.AcquireType;
import org.mmocore.gameserver.model.base.ClassId;
import org.mmocore.gameserver.model.pledge.Clan;
import org.mmocore.gameserver.model.pledge.SubUnit;

import java.util.*;

/**
 * @author: VISTALL
 * @date: 20:55/30.11.2010
 */
public final class SkillAcquireHolder extends AbstractHolder
{
	private static final SkillAcquireHolder _instance = new SkillAcquireHolder();

	public static SkillAcquireHolder getInstance()
	{
		return _instance;
	}

	// классовые зависимости
	private static HashMap<Integer, List<SkillLearn>> _normalSkillTree = new HashMap<Integer, List<SkillLearn>>();
	private static HashMap<Integer, List<SkillLearn>> _transferSkillTree = new HashMap<Integer, List<SkillLearn>>();
	// расовые зависимости
	private static HashMap<Integer, List<SkillLearn>> _fishingSkillTree = new HashMap<Integer, List<SkillLearn>>();
	private static HashMap<Integer, List<SkillLearn>> _transformationSkillTree = new HashMap<Integer, List<SkillLearn>>();
	private static HashMap<Integer, List<SkillLearn>> _raceSkillTree = new HashMap<Integer, List<SkillLearn>>();
	// без зависимостей
	private static GArray<SkillLearn> _certificationSkillTree = new GArray<SkillLearn>();
	private static GArray<SkillLearn> _collectionSkillTree = new GArray<SkillLearn>();
	private static GArray<SkillLearn> _pledgeSkillTree = new GArray<SkillLearn>();
	private static GArray<SkillLearn> _subUnitSkillTree = new GArray<SkillLearn>();

	public int getMinLevelForNewSkill(Player player, AcquireType type)
	{
		GArray<SkillLearn> skills = new GArray<SkillLearn>();
		switch (type)
		{
			case NORMAL:
			{
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));

				// Рассовые бесплатные скилы, зависещие только от рассы и уровня
				// игрока.
				if(player.isAwaking())
					skills.addAll(_raceSkillTree.get(player.getRace().ordinal()));

				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return 0;
				}
			}
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return 0;
				}
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return 0;
				}
				break;
			default:
				return 0;
		}
		int minlevel = 0;
		for (SkillLearn temp : skills)
			if (temp.getMinLevel() > player.getLevel())
				if (minlevel == 0 || temp.getMinLevel() < minlevel)
					minlevel = temp.getMinLevel();
		return minlevel;
	}

	public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type)
	{
		return getAvailableSkills(player, type, null);
	}

	public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type, SubUnit subUnit)
	{
		GArray<SkillLearn> skills = new GArray<SkillLearn>();
		switch (type)
		{
			case NORMAL:
			{
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));

				// Рассовые бесплатные скилы, зависещие только от рассы и уровня
				// игрока.
				if(player.isAwaking())
					skills.addAll(_raceSkillTree.get(player.getRace().ordinal()));

				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			}
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case TRANSFER_EVA_SAINTS:
			case TRANSFER_SHILLIEN_SAINTS:
			case TRANSFER_CARDINAL:
				skills.addAll(_transferSkillTree.get(type.transferClassId()));
				if (skills.isEmpty())
				{
					info("skill tree for class " + type.transferClassId() + " is not defined !");
					return Collections.emptyList();
				}
				if (player == null)
					return skills;
				else
				{
					List<SkillLearn> skillLearnMap = new ArrayList<SkillLearn>();
					for (SkillLearn temp : skills)
						if (temp.getMinLevel() <= player.getLevel())
						{
							int knownLevel = player.getSkillLevel(temp.getId());
							if (knownLevel == -1)
								skillLearnMap.add(temp);
						}

					return skillLearnMap;
				}
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case CLAN:
				skills.addAll(_pledgeSkillTree);
				Collection<Skill> skls = player.getClan().getSkills(); // TODO
				// [VISTALL]
				// придумать
				// другой
				// способ

				return getAvaliableList(skills, skls.toArray(new Skill[skls.size()]), player.getClan().getLevel());
			case SUB_UNIT:
				skills.addAll(_subUnitSkillTree);
				Collection<Skill> st = subUnit.getSkills(); // TODO [VISTALL]
				// придумать другой
				// способ

				return getAvaliableList(skills, st.toArray(new Skill[st.size()]), player.getClan().getLevel());
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				if (player == null)
					return skills;
				else
					return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			default:
				return Collections.emptyList();
		}
	}

	public Collection<SkillLearn> getAvailableAllSkills(Player player)
	{
		GArray<SkillLearn> skills = new GArray<SkillLearn>();

		skills.addAll(_normalSkillTree.get(player.getActiveClassId()));

		// Рассовые бесплатные скилы, зависещие только от рассы и уровня игрока.
		if(player.isAwaking())
			skills.addAll(_raceSkillTree.get(player.getRace().ordinal()));

		if (skills.isEmpty())
		{
			info("skill tree for class " + player.getActiveClassId() + " is not defined !");
			return Collections.emptyList();
		}
		return getAvaliableAllList(skills, player.getAllSkillsArray(), player.getLevel());
	}

	private Collection<SkillLearn> getAvaliableAllList(Collection<SkillLearn> skillLearns, Skill[] skills, int level)
	{
		Set<SkillLearn> skillLearnMap = new HashSet<SkillLearn>();
		loop: for (SkillLearn temp : skillLearns)
		{
			for (Skill s : skills)
			{
				if (temp.getId() == s.getId())
				{
					if (temp.getLevel() - 1 == s.getLevel())
						skillLearnMap.add(temp);
					continue loop;
				}
				if (s.isRelationSkill())
					for (int ds : s.getRelationSkills())
						if (temp.getId() == ds)
							continue loop;

			}
			if (temp.getLevel() == 1)
				skillLearnMap.add(temp);
		}

		return skillLearnMap;
	}

	private Collection<SkillLearn> getAvaliableList(final Collection<SkillLearn> skillLearns, Skill[] skills, int level)
	{
		Set<SkillLearn> skillLearnMap = new HashSet<SkillLearn>();
		for (SkillLearn temp : skillLearns)
			if (temp.getMinLevel() <= level)
			{
				boolean knownSkill = false;
				m: for (int j = 0; j < skills.length && !knownSkill; j++)
				{
					if (skills[j].isRelationSkill())
						for (int _k : skills[j].getRelationSkills())
							if (temp.getId() == _k)
							{
								knownSkill = true;
								break m;
							}
					if (skills[j].getId() == temp.getId())
					{
						knownSkill = true;
						if (skills[j].getLevel() == temp.getLevel() - 1)
							skillLearnMap.add(temp);
					}
				}
				if (!knownSkill && temp.getLevel() == 1)
					skillLearnMap.add(temp);
			}

		return skillLearnMap;
	}

	public SkillLearn getSkillLearn(Player player, int id, int level, AcquireType type)
	{
		GArray<SkillLearn> skills = new GArray<SkillLearn>();
		switch (type)
		{
			case NORMAL:
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));

				// Рассовые бесплатные скилы, зависещие только от рассы и уровня
				// игрока.
				if(player.isAwaking())
					skills.addAll(_raceSkillTree.get(player.getRace().ordinal()));

				break;
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				break;
			case TRANSFER_CARDINAL:
			case TRANSFER_SHILLIEN_SAINTS:
			case TRANSFER_EVA_SAINTS:
				skills.addAll(_transferSkillTree.get(player.getActiveClassId()));
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				break;
			case CLAN:
				skills.addAll(_pledgeSkillTree);
				break;
			case SUB_UNIT:
				skills.addAll(_subUnitSkillTree);
				break;
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				break;
			default:
				return null;
		}

		if (skills.isEmpty())
			return null;

		for (SkillLearn temp : skills)
			if (temp.getLevel() == level && temp.getId() == id)
				return temp;

		return null;
	}

	public boolean isSkillPossible(Player player, Skill skill, AcquireType type)
	{
		Clan clan = null;
		GArray<SkillLearn> skills = new GArray<SkillLearn>();
		switch (type)
		{
			case NORMAL:
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
				// Рассовые бесплатные скилы, зависещие только от рассы и уровня
				// игрока.
				if(player.isAwaking())
					skills.addAll(_raceSkillTree.get(player.getRace().ordinal()));
				break;
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				break;
			case TRANSFER_CARDINAL:
			case TRANSFER_EVA_SAINTS:
			case TRANSFER_SHILLIEN_SAINTS:
				int transferId = type.transferClassId();
				if (player.getActiveClassId() != transferId)
					return false;

				skills.addAll(_transferSkillTree.get(transferId));
				break;
			case CLAN:
				clan = player.getClan();
				if (clan == null)
					return false;
				skills.addAll(_pledgeSkillTree);
				break;
			case SUB_UNIT:
				clan = player.getClan();
				if (clan == null)
					return false;

				skills.addAll(_subUnitSkillTree);
				break;
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				break;
			default:
				return false;
		}

		return isSkillPossible(skills, skill);
	}

	private boolean isSkillPossible(final Collection<SkillLearn> skills, Skill skill)
	{
		for (SkillLearn learn : skills)
			if (learn.getId() == skill.getId() && learn.getLevel() <= skill.getLevel())
				return true;
		return false;
	}

	public boolean isSkillPossible(Player player, Skill skill)
	{
		for (AcquireType aq : AcquireType.VALUES)
			if (isSkillPossible(player, skill, aq))
				return true;

		return false;
	}

	public List<SkillLearn> getSkillLearnListByItemId(Player player, int itemId)
	{
		List<SkillLearn> learns = _normalSkillTree.get(player.getActiveClassId());
		if (learns == null)
			return Collections.emptyList();

		List<SkillLearn> l = new ArrayList<SkillLearn>(1);
		for (SkillLearn $i : learns)
			if ($i.getItemId() == itemId)
				l.add($i);

		return l;
	}

	public List<SkillLearn> getAllNormalSkillTreeWithForgottenScrolls()
	{
		List<SkillLearn> a = new ArrayList<SkillLearn>();
		for (List<SkillLearn> i : _normalSkillTree.values())
			for (SkillLearn learn : i.toArray(new SkillLearn[i.size()]))
				if (learn.getItemId() > 0 && learn.isClicked())
					a.add(learn);

		return a;
	}

	public void addAllNormalSkillLearns(HashMap<Integer, List<SkillLearn>> map)
	{
		int classID;

		for (ClassId classId : ClassId.VALUES)
		{
			if (classId.name().startsWith("DUMMY_ENTRY"))
				continue;

			classID = classId.getId();

			List<SkillLearn> temp;

			temp = map.get(classID);
			if (temp == null)
			{
				info("Not found NORMAL skill learn for class " + classID);
				continue;
			}

			_normalSkillTree.put(classId.getId(), temp);

			ClassId secondparent = classId.getParent(1);
			if (secondparent == classId.getParent(0))
				secondparent = null;

			classId = classId.getParent(0);

			while (classId != null)
			{
				List<SkillLearn> parentList = _normalSkillTree.get(classId.getId());
				temp.addAll(parentList);

				classId = classId.getParent(0);
				if (classId == null && secondparent != null)
				{
					classId = secondparent;
					secondparent = secondparent.getParent(1);
				}
			}
		}
	}

	public void addAllFishingLearns(int race, List<SkillLearn> s)
	{
		_fishingSkillTree.put(race, s);
	}

	public void addAllTransferLearns(int classId, List<SkillLearn> s)
	{
		_transferSkillTree.put(classId, s);
	}

	public void addAllTransformationLearns(int race, List<SkillLearn> s)
	{
		_transformationSkillTree.put(race, s);
	}

	public void addAllCertificationLearns(List<SkillLearn> s)
	{
		_certificationSkillTree.addAll(s);
	}

	public void addAllCollectionLearns(List<SkillLearn> s)
	{
		_collectionSkillTree.addAll(s);
	}

	public void addAllSubUnitLearns(List<SkillLearn> s)
	{
		_subUnitSkillTree.addAll(s);
	}

	public void addAllPledgeLearns(List<SkillLearn> s)
	{
		_pledgeSkillTree.addAll(s);
	}

	public void addAllRaceLearns(int race, List<SkillLearn> s)
	{
		_raceSkillTree.put(race, s);
	}

	@Override
	public void log()
	{
		info("load " + sizeHashMap(_normalSkillTree) + " normal learns for " + _normalSkillTree.size() + " classes.");
		info("load " + sizeHashMap(_transferSkillTree) + " transfer learns for " + _transferSkillTree.size() + " classes.");
		info("load " + sizeHashMap(_raceSkillTree) + " race learns for " + _raceSkillTree.size() + " races.");
		//
		info("load " + sizeHashMap(_transformationSkillTree) + " transformation learns for " + _transformationSkillTree.size() + " races.");
		info("load " + sizeHashMap(_fishingSkillTree) + " fishing learns for " + _fishingSkillTree.size() + " races.");
		//
		info("load " + _certificationSkillTree.size() + " certification learns.");
		info("load " + _collectionSkillTree.size() + " collection learns.");
		info("load " + _pledgeSkillTree.size() + " pledge learns.");
		info("load " + _subUnitSkillTree.size() + " sub unit learns.");
	}

	@Deprecated
	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		_normalSkillTree.clear();
		_fishingSkillTree.clear();
		_transferSkillTree.clear();
		_certificationSkillTree.clear();
		_collectionSkillTree.clear();
		_pledgeSkillTree.clear();
		_subUnitSkillTree.clear();
		_raceSkillTree.clear();
	}

	private int sizeHashMap(HashMap<Integer, List<SkillLearn>> a)
	{
		int i = 0;
		for (List<SkillLearn> iterator : a.values())
			i += iterator.size();

		return i;
	}
}