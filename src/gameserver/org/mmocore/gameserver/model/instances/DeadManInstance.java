package org.mmocore.gameserver.model.instances;

import org.mmocore.gameserver.ai.CharacterAI;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.network.serverpackets.Die;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

public class DeadManInstance extends NpcInstance
{
	/**
     *
     */
	private static final long serialVersionUID = 6018606494042957815L;

	public DeadManInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setAI(new CharacterAI(this));
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		setCurrentHp(0, false);
		broadcastPacket(new Die(this));
		setWalking();
	}

	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage,
	        boolean isDot, boolean sendMessage)
	{
	}

	@Override
	public boolean isInvul()
	{
		return true;
	}

	@Override
	public boolean isBlocked()
	{
		return true;
	}
}