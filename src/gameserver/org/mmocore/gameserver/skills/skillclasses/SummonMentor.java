package org.mmocore.gameserver.skills.skillclasses;

import org.mmocore.commons.collections.GArray;
import org.mmocore.gameserver.cache.Msg;
import org.mmocore.gameserver.listener.actor.player.impl.MentorAnswerListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.Zone;
import org.mmocore.gameserver.model.base.TeamType;
import org.mmocore.gameserver.network.serverpackets.ConfirmDlg;
import org.mmocore.gameserver.network.serverpackets.components.CustomMessage;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.templates.StatsSet;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 10.05.12 Time: 0:03
 */
public class SummonMentor extends Skill {
    public SummonMentor(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!activeChar.isPlayer())
            return false;

        if (activeChar.isPlayer()) {
            Player p = (Player) activeChar;
            if (p.getActiveWeaponFlagAttachment() != null) {
                activeChar.sendPacket(SystemMsg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
                return false;
            }
            if (p.isInDuel() || p.getTeam() != TeamType.NONE) {
                activeChar.sendMessage(new CustomMessage("common.RecallInDuel", p));
                return false;
            }
            if (p.isInOlympiadMode()) {
                activeChar.sendPacket(Msg.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
                return false;
            }
        }

        if (activeChar.isInZone(Zone.ZoneType.no_escape) && activeChar.getReflection() != null && activeChar.getReflection().getCoreLoc() != null) {
            if (activeChar.isPlayer())
                activeChar.sendMessage(new CustomMessage("org.mmocore.gameserver.skills.skillclasses.Recall.Here", (Player) activeChar));
            return false;
        }

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature activeChar, GArray<Creature> targets) {
        if (!activeChar.isPlayer())
            return;

        Player player = (Player) activeChar;
        int mentorId = player.getMentorSystem().getMentor();
        Player mentor;
        if (mentorId != 0) {
            mentor = World.getPlayer(mentorId);
            if (mentor == null)
                return;
        } else
            return;

        mentor.ask(new ConfirmDlg(SystemMsg.S1, 30000).addString("Teleport to Mentee? " + activeChar.getName()), new MentorAnswerListener(mentor, activeChar.getName()));

    }
}