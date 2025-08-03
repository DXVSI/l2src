package npc.model;

import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.network.serverpackets.ExShowScreenMessage;
import org.mmocore.gameserver.network.serverpackets.MagicSkillUse;
import org.mmocore.gameserver.network.serverpackets.NpcSay;
import org.mmocore.gameserver.network.serverpackets.components.ChatType;
import org.mmocore.gameserver.network.serverpackets.components.NpcString;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.npc.NpcTemplate;
import org.mmocore.gameserver.utils.NpcUtils;

public class KukuruInstance extends NpcInstance
{
	private static final long serialVersionUID = 2769307524084003609L;
	private int Kookaru = 33200;

	public KukuruInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.startsWith("gokukuru"))
		{
			if(player.getEffectList().getEffectsBySkillId(9209) == null)
			{
				Skill skill = SkillTable.getInstance().getInfo(9209, 1);
				player.altUseSkill(skill, player);
				player.broadcastPacket(new MagicSkillUse(player, player, skill.getId(), 1, 0, 0));
			}
			else
				this.broadcastPacket(new NpcSay(this, ChatType.NPC_SAY, NpcString.YOU_CAN_T_RIDE_A_KOOKARU_NOW));
		}
		if(command.startsWith("gokukururace"))
		{
			if(player.getEffectList().getEffectsBySkillId(9209) == null)
			{
				Skill skill = SkillTable.getInstance().getInfo(9209, 1);
				player.altUseSkill(skill, player);
				player.broadcastPacket(new MagicSkillUse(player, player, skill.getId(), 1, 0, 0));
				NpcUtils.spawnSingle(Kookaru, -109752, 246920, -3011);
				this.broadcastPacket(new ExShowScreenMessage(NpcString.YOU_WIN_IF_YOU_GET_TO_YE_SAGIRA_RUINS_FIRST, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else
				this.broadcastPacket(new NpcSay(this, ChatType.NPC_SAY, NpcString.YOU_CAN_T_RIDE_A_KOOKARU_NOW));
		}
		else
			super.onBypassFeedback(player, command);
	}

}