package ai;

import org.mmocore.gameserver.ai.Fighter;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.NpcInfo;
import org.mmocore.gameserver.scripts.Functions;

public class MobPrison extends Fighter
{
	private int time = 18;
	
	public MobPrison(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		
		actor.setTitle(String.valueOf(time));
		time--;
		
		for (Player player : World.getAroundPlayers(actor))
				player.sendPacket(new NpcInfo(actor, player));
				
		if (time <= 0)
		{
			if (actor.getNpcId() == 22980)
				Functions.spawn(actor.getLoc(), 22966);
			else if (actor.getNpcId() == 22979)
				Functions.spawn(actor.getLoc(), 22965);
			else if (actor.getNpcId() == 22981)
				Functions.spawn(actor.getLoc(), 22967);
			actor.deleteMe();	
		}

		return super.thinkActive();
	}

}