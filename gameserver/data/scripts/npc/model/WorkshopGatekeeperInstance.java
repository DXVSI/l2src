package npc.model;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.base.ClassId;
import org.mmocore.gameserver.model.instances.DoorInstance;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.NpcHtmlMessage;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.ReflectionUtils;

/**
 * Открывает двери 5го этажа Tully Workshop
 *
 * @author pchayka
 */
public class WorkshopGatekeeperInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 31447859416769360L;
	private static long doorRecharge = 0;
	private static final TIntObjectHashMap<TIntHashSet> doors = new TIntObjectHashMap<TIntHashSet>();

	static
	{
		TIntHashSet list;

		list = new TIntHashSet();
		list.add(19260001);
		list.add(19260002);
		doors.put(18445, list);

		list = new TIntHashSet();
		list.add(19260003);
		doors.put(18446, list);

		list = new TIntHashSet();
		list.add(19260003);
		list.add(19260004);
		list.add(19260005);
		doors.put(18447, list);

		list = new TIntHashSet();
		list.add(19260006);
		list.add(19260007);
		doors.put(18448, list);

		list = new TIntHashSet();
		list.add(19260007);
		list.add(19260008);
		doors.put(18449, list);

		list = new TIntHashSet();
		list.add(19260010);
		doors.put(18450, list);

		list = new TIntHashSet();
		list.add(19260011);
		list.add(19260012);
		doors.put(18451, list);

		list = new TIntHashSet();
		list.add(19260009);
		list.add(19260011);
		doors.put(18452, list);

		list = new TIntHashSet();
		list.add(19260014);
		list.add(19260023);
		list.add(19260013);
		doors.put(18453, list);

		list = new TIntHashSet();
		list.add(19260015);
		list.add(19260023);
		doors.put(18454, list);

		list = new TIntHashSet();
		list.add(19260016);
		list.add(19260019); //coded hard
		doors.put(18455, list);

		list = new TIntHashSet();
		list.add(19260017);
		list.add(19260018);
		doors.put(18456, list);

		list = new TIntHashSet();
		list.add(19260021);
		list.add(19260020);
		doors.put(18457, list);

		list = new TIntHashSet();
		list.add(19260022);
		doors.put(18458, list);

		list = new TIntHashSet();
		list.add(19260018);
		doors.put(18459, list);

		list = new TIntHashSet();
		list.add(19260051);
		doors.put(18460, list);

		list = new TIntHashSet();
		list.add(19260052);
		doors.put(18461, list);
	}

	public WorkshopGatekeeperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.startsWith("trydoor"))
		{
			int npcId = getNpcId();
			if(doorRecharge == 0 || doorRecharge <= System.currentTimeMillis())
			{
				if(player.getClassId() == ClassId.MAESTRO)
				{
					openDoor(npcId);
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>Doors are opened."));
				}
				else if(Rnd.chance(60)) //unknown
				{
					openDoor(npcId);
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>Doors are opened."));
				}
				else
				{
					doorRecharge = System.currentTimeMillis() + 120 * 1000L; // 120 sec retail
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>The attempt has failed. Recharching..."));
				}
			}
			else
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>The time needed for the recharge has not elapsed yet"));
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		return "default/18445.htm";
	}

	private void openDoor(int npcId)
	{
		TIntHashSet set = doors.get(npcId);
		if(set != null)
			for(int i : set.toArray())
			{
				DoorInstance doorToOpen = ReflectionUtils.getDoor(i);
				doorToOpen.openMe();
				ThreadPoolManager.getInstance().schedule(new DoorClose(doorToOpen), 120 * 1000L);
			}
	}

	private class DoorClose extends RunnableImpl
	{
		DoorInstance _door;

		public DoorClose(DoorInstance door)
		{
			_door = door;
		}

		@Override
		public void runImpl() throws Exception
		{
			_door.closeMe();
		}
	}
}