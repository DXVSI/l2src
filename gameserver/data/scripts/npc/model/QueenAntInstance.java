package npc.model;

import java.util.ArrayList;
import java.util.List;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.data.xml.holder.NpcHolder;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.SimpleSpawner;
import org.mmocore.gameserver.model.instances.BossInstance;
import org.mmocore.gameserver.model.instances.MinionInstance;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.PlaySound;
import org.mmocore.gameserver.scripts.Functions;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.Location;

public class QueenAntInstance extends BossInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1736191173721863916L;

	private static final int Queen_Ant_Larva = 29002;

	private List<SimpleSpawner> _spawns = new ArrayList<SimpleSpawner>();
	private NpcInstance Larva = null;

	public QueenAntInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public NpcInstance getLarva()
	{
		if(Larva == null)
			Larva = SpawnNPC(Queen_Ant_Larva, new Location(-21600, 179482, -5846, Rnd.get(0, 0xFFFF)));
		return Larva;
	}

	@Override
	protected int getKilledInterval(MinionInstance minion)
	{
		return minion.getNpcId() == 29003 ? 10000 : 280000 + Rnd.get(40000);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, getLoc()));
		Functions.deSpawnNPCs(_spawns);
		Larva = null;
		super.onDeath(killer);
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getLarva();
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS01_A", 1, 0, getLoc()));
	}

	private NpcInstance SpawnNPC(int npcId, Location loc)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		if(template == null)
		{
			System.out.println("WARNING! template is null for npc: " + npcId);
			Thread.dumpStack();
			return null;
		}
		try
		{
			SimpleSpawner sp = new SimpleSpawner(template);
			sp.setLoc(loc);
			sp.setAmount(1);
			sp.setRespawnDelay(0);
			_spawns.add(sp);
			return sp.spawnOne();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}