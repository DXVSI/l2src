package npc.model.residences.clanhall;

import npc.model.residences.SiegeGuardInstance;

import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 17:50/13.05.2011
 */
public abstract class _34BossMinionInstance extends SiegeGuardInstance implements _34SiegeGuard
{
	/**
	* 
	*/
	private static final long serialVersionUID = -387219961406201474L;

	public _34BossMinionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onDeath(Creature killer)
	{
		setCurrentHp(1, true);
	}

	@Override
	public void onSpawn()
	{
		super.onSpawn();

		Functions.npcShout(this, spawnChatSay());
	}

	public abstract NpcString spawnChatSay();

	@Override
	public abstract NpcString teleChatSay();
}
