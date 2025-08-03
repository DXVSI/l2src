package ai;

import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.GameObject;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.SocialAction;


public class ArcanCat extends Fighter
{
	
	public ArcanCat(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 5000;
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		
		actor.broadcastPacket(new SocialAction(actor.getObjectId(), 2));

		return super.thinkActive();
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}

}