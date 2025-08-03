package org.mmocore.gameserver.network.clientpackets;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.instancemanager.AwakingManager;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.base.ProfessionRewards;
import org.mmocore.gameserver.model.base.ClassId;
import org.mmocore.gameserver.network.serverpackets.ExShowUsmVideo;
import org.mmocore.gameserver.utils.ItemFunctions;


/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.01.12 Time: 16:32
 */
public class RequestChangeToAwakenedClass extends L2GameClientPacket
{
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	//private static final int STONE_OF_DESTINY = 17722;
	private int change;
	private int MY_CLASS_ID;
	private int MY_OLD_CLASS_ID;

	@Override
	protected void readImpl() throws Exception
	{
		this.change = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		if(!Config.AWAKING_FREE)
		{
			final Player player = getClient().getActiveChar();
			if(player == null)
				return;
			if(change != 1)
				return;
			ClassId classId = player.getClassId();
			for (ClassId cid : ClassId.VALUES)
			{
				if(cid.childOf(classId) && cid.getClassLevel().ordinal() == classId.getClassLevel().ordinal() + 1)
				{
					if(player.getInventory().getCountOf(SCROLL_OF_AFTERLIFE) > 0)
					{
						ItemFunctions.removeItem(player, SCROLL_OF_AFTERLIFE, 1, true);
						if(player.getVar("AwakenedID") != null)
						{
							MY_CLASS_ID = player.getVarInt("AwakenedID");
							ProfessionRewards.checkedAndGiveChest(player, MY_CLASS_ID);
						}
						if(player.getVar("AwakenedOldIDClass") != null)
						{
							MY_OLD_CLASS_ID = player.getVarInt("AwakenedOldIDClass");
							ProfessionRewards.checkedAndGiveWeapon(player, MY_OLD_CLASS_ID);
						}
						AwakingManager.getInstance().SetAwakingId(player);
						ThreadPoolManager.getInstance().schedule(new RunnableImpl()
						{
							@Override
							public void runImpl() throws Exception
							{
								player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
							}
						}, 15000);
					} /*
					 * else { if (player.getInventory().getCountOf(STONE_OF_DESTINY) > 0 && player.getInventory().getCountOf(SCROLL_OF_AFTERLIFE) > 0)
					 * { player.getInventory().removeItemByItemId(SCROLL_OF_AFTERLIFE , 1); AwakingManager.getInstance().SetAwakingId2(player);
					 * ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
					 * 
					 * @Override public void runImpl() throws Exception { player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010)); } }, 15000); } }
					 */// TODO
					return;
				}
			}
		}
		else
		{
			Player player = getClient().getActiveChar();

			if(player == null)
				return;

			if(player.getLevel() < 85)
				return;

			if(player.getClassId().level() < 3)
				return;

			if(player.isAwaking())
				return;

			AwakingManager.getInstance().SetAwakingId(player);
		}
	}
}
