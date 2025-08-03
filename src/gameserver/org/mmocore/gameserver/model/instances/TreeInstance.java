package org.mmocore.gameserver.model.instances;

import org.mmocore.commons.collections.GArray;
import org.mmocore.commons.lang.reference.HardReference;
import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.*;
import org.mmocore.gameserver.network.serverpackets.MagicSkillUse;
import org.mmocore.gameserver.taskmanager.EffectTaskManager;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;

import java.util.concurrent.ScheduledFuture;

/**
 * @author ALF
 * @data 05.06.2012
 */
public class TreeInstance extends NpcInstance
{
	private static final long serialVersionUID = -3990686488577795700L;
	private final Player _owner;
	private final Skill _skill;
	private int _lifetimeCountdown;
	private ScheduledFuture<?> _targetTask;
	private ScheduledFuture<?> _destroyTask;

	public TreeInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill)
	{
		this(objectId, template, owner, lifetime, skill, owner.getLoc());
	}

	public TreeInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill, Location loc)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		_lifetimeCountdown = lifetime;
		setLevel(owner.getLevel());
		setTitle(owner.getName());
		setLoc(loc);
	}

	public Player getOwner()
	{
		return _owner;
	}

	private static class CastTask extends RunnableImpl
	{
		private HardReference<NpcInstance> _trapRef;

		public CastTask(TreeInstance trap)
		{
			_trapRef = trap.getRef();
		}

		@Override
		public void runImpl() throws Exception
		{
			TreeInstance tree = (TreeInstance) _trapRef.get();

			if (tree == null)
				return;

			Player owner = tree.getOwner();
			if (owner == null)
				return;

			GArray<Creature> targets = new GArray<Creature>(10);
			for (Player target : World.getAroundPlayers(tree, 600, 200))
			{
				if (targets.size() > 10)
					break;
				if (target == owner)
				{
					targets.add(target);
					tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), 0, 0));
				}
				if (target.getParty() != null && owner.getParty() == target.getParty())
				{
					targets.add(target);
					tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), 0, 0));
				}
			}
			tree.callSkill(tree._skill, targets, true);
		}
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();

		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), _lifetimeCountdown);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new CastTask(this), 5000L, 5000L);
	}

	@Override
	protected void onDelete()
	{
		Player owner = getOwner();
		if (owner != null)
			owner.setTree(false);
		if (_destroyTask != null)
			_destroyTask.cancel(false);
		_destroyTask = null;
		if (_targetTask != null)
			_targetTask.cancel(false);
		_targetTask = null;
		super.onDelete();
	}

	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}

	@Override
	public boolean isInvul()
	{
		return true;
	}

	@Override
	public boolean isFearImmune()
	{
		return true;
	}

	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}

	@Override
	public boolean isLethalImmune()
	{
		return true;
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}

	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		player.sendActionFailed();
	}
}
