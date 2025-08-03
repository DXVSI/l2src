package org.mmocore.gameserver.network.clientpackets;

import org.apache.commons.lang3.tuple.Pair;
import org.mmocore.gameserver.Announcements;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.dao.MailDAO;
import org.mmocore.gameserver.data.StringHolder;
import org.mmocore.gameserver.data.xml.holder.ResidenceHolder;
import org.mmocore.gameserver.instancemanager.AwakingManager;
import org.mmocore.gameserver.instancemanager.CoupleManager;
import org.mmocore.gameserver.instancemanager.CursedWeaponsManager;
import org.mmocore.gameserver.instancemanager.PetitionManager;
import org.mmocore.gameserver.instancemanager.PlayerMessageStack;
import org.mmocore.gameserver.instancemanager.QuestManager;
import org.mmocore.gameserver.listener.actor.player.OnAnswerListener;
import org.mmocore.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.Summon;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.base.ClassLevel;
import org.mmocore.gameserver.model.base.InvisibleType;
import org.mmocore.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import org.mmocore.gameserver.model.entity.residence.ClanHall;
import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.model.mail.Mail;
import org.mmocore.gameserver.model.party.PartySubstitute;
import org.mmocore.gameserver.model.pledge.Clan;
import org.mmocore.gameserver.model.pledge.SubUnit;
import org.mmocore.gameserver.model.pledge.UnitMember;
import org.mmocore.gameserver.model.quest.Quest;
import org.mmocore.gameserver.network.GameClient;
import org.mmocore.gameserver.network.serverpackets.ChangeWaitType;
import org.mmocore.gameserver.network.serverpackets.ClientSetTime;
import org.mmocore.gameserver.network.serverpackets.ConfirmDlg;
import org.mmocore.gameserver.network.serverpackets.Die;
import org.mmocore.gameserver.network.serverpackets.EtcStatusUpdate;
import org.mmocore.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import org.mmocore.gameserver.network.serverpackets.ExAutoSoulShot;
import org.mmocore.gameserver.network.serverpackets.ExBR_PremiumState;
import org.mmocore.gameserver.network.serverpackets.ExBasicActionList;
import org.mmocore.gameserver.network.serverpackets.ExChangeMPCost;
import org.mmocore.gameserver.network.serverpackets.ExGoodsInventoryChangedNotify;
import org.mmocore.gameserver.network.serverpackets.ExMPCCOpen;
import org.mmocore.gameserver.network.serverpackets.ExNoticePostArrived;
import org.mmocore.gameserver.network.serverpackets.ExNotifyPremiumItem;
import org.mmocore.gameserver.network.serverpackets.ExPCCafePointInfo;
import org.mmocore.gameserver.network.serverpackets.ExReceiveShowPostFriend;
import org.mmocore.gameserver.network.serverpackets.ExSetCompassZoneCode;
import org.mmocore.gameserver.network.serverpackets.ExShowUsmVideo;
import org.mmocore.gameserver.network.serverpackets.ExStorageMaxCount;
import org.mmocore.gameserver.network.serverpackets.ExSubjobInfo;
import org.mmocore.gameserver.network.serverpackets.ExTutorialList;
import org.mmocore.gameserver.network.serverpackets.ExVitalityEffectInfo;
import org.mmocore.gameserver.network.serverpackets.HennaInfo;
import org.mmocore.gameserver.network.serverpackets.L2FriendList;
import org.mmocore.gameserver.network.serverpackets.L2GameServerPacket;
import org.mmocore.gameserver.network.serverpackets.MagicSkillLaunched;
import org.mmocore.gameserver.network.serverpackets.MagicSkillUse;
import org.mmocore.gameserver.network.serverpackets.PartySmallWindowAll;
import org.mmocore.gameserver.network.serverpackets.PartySpelled;
import org.mmocore.gameserver.network.serverpackets.PetInfo;
import org.mmocore.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import org.mmocore.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import org.mmocore.gameserver.network.serverpackets.PledgeSkillList;
import org.mmocore.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import org.mmocore.gameserver.network.serverpackets.PrivateStoreMsgSell;
import org.mmocore.gameserver.network.serverpackets.QuestList;
import org.mmocore.gameserver.network.serverpackets.RecipeShopMsg;
import org.mmocore.gameserver.network.serverpackets.RelationChanged;
import org.mmocore.gameserver.network.serverpackets.Ride;
import org.mmocore.gameserver.network.serverpackets.SSQInfo;
import org.mmocore.gameserver.network.serverpackets.ShortCutInit;
import org.mmocore.gameserver.network.serverpackets.SkillCoolTime;
import org.mmocore.gameserver.network.serverpackets.SkillList;
import org.mmocore.gameserver.network.serverpackets.SystemMessage2;
import org.mmocore.gameserver.network.serverpackets.components.SystemMsg;
import org.mmocore.gameserver.skills.AbnormalEffect;
import org.mmocore.gameserver.skills.effects.EffectTemplate;
import org.mmocore.gameserver.stats.Env;
import org.mmocore.gameserver.tables.SkillTable;
import org.mmocore.gameserver.templates.item.ItemTemplate;
import org.mmocore.gameserver.utils.GameStats;
import org.mmocore.gameserver.utils.ItemFunctions;
import org.mmocore.gameserver.utils.TradeHelper;

import java.util.Calendar;

public class EnterWorld extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        // readS(); - клиент всегда отправляет строку "narcasse"
    }

    @Override
    protected void runImpl() {
        GameClient client = getClient();
        Player activeChar = client.getActiveChar();

        if (activeChar == null) {
            client.closeNow(false);
            return;
        }

        GameStats.incrementPlayerEnterGame();

        boolean first = activeChar.entering;

        if (first) {
            activeChar.setOnlineStatus(true);
            if (activeChar.getPlayerAccess().GodMode && !Config.SHOW_GM_LOGIN)
                activeChar.setInvisibleType(InvisibleType.NORMAL);

            activeChar.setNonAggroTime(Long.MAX_VALUE);
            activeChar.spawnMe();

            if (activeChar.isInStoreMode())
                if (!TradeHelper.checksIfCanOpenStore(activeChar, activeChar.getPrivateStoreType())) {
                    activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                    activeChar.standUp();
                    activeChar.broadcastCharInfo();
                }

            activeChar.setRunning();
            activeChar.standUp();
            activeChar.startTimers();
        }

        activeChar.sendPacket(new ExBR_PremiumState(activeChar, activeChar.hasBonus()));

        activeChar.getMacroses().sendAllUpdate();
        activeChar.sendPacket(new SSQInfo(), new HennaInfo(activeChar));
        activeChar.sendItemList(false);
        activeChar.sendPacket(new ShortCutInit(activeChar), new SkillList(activeChar), new SkillCoolTime(activeChar));

        activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));

        activeChar.sendPacket(SystemMsg.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);

        Announcements.getInstance().showAnnouncements(activeChar);

        if (first)
            activeChar.getListeners().onEnter();

        if (first && activeChar.getCreateTime() > 0) {
            Calendar create = Calendar.getInstance();
            create.setTimeInMillis(activeChar.getCreateTime());
            Calendar now = Calendar.getInstance();

            int day = create.get(Calendar.DAY_OF_MONTH);
            if (create.get(Calendar.MONTH) == Calendar.FEBRUARY && day == 29)
                day = 28;

            int myBirthdayReceiveYear = activeChar.getVarInt(Player.MY_BIRTHDAY_RECEIVE_YEAR, 0);
            if (create.get(Calendar.MONTH) == now.get(Calendar.MONTH) && create.get(Calendar.DAY_OF_MONTH) == day)
                if (myBirthdayReceiveYear == 0 && create.get(Calendar.YEAR) != now.get(Calendar.YEAR) || myBirthdayReceiveYear > 0 && myBirthdayReceiveYear != now.get(Calendar.YEAR)) {
                    Mail mail = new Mail();
                    mail.setSenderId(1);
                    mail.setSenderName(StringHolder.getInstance().getNotNull(activeChar, "birthday.npc"));
                    mail.setReceiverId(activeChar.getObjectId());
                    mail.setReceiverName(activeChar.getName());
                    mail.setTopic(StringHolder.getInstance().getNotNull(activeChar, "birthday.title"));
                    mail.setBody(StringHolder.getInstance().getNotNull(activeChar, "birthday.text"));

                    ItemInstance item = ItemFunctions.createItem(21169);
                    item.setLocation(ItemInstance.ItemLocation.MAIL);
                    item.setCount(1L);
                    item.save();

                    mail.addAttachment(item);
                    mail.setUnread(true);
                    mail.setType(Mail.SenderType.BIRTHDAY);
                    mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
                    mail.save();

                    activeChar.setVar(Player.MY_BIRTHDAY_RECEIVE_YEAR, String.valueOf(now.get(Calendar.YEAR)), -1);
                }
        }

        if (activeChar.getClan() != null) {
            notifyClanMembers(activeChar);

            activeChar.sendPacket(activeChar.getClan().listAll());
            activeChar.sendPacket(new PledgeShowInfoUpdate(activeChar.getClan()), new PledgeSkillList(activeChar.getClan()));

            if (activeChar.isClanLeader() & activeChar.getClan().getLevel() >= 5)
                for (Player allmembers : activeChar.getClan().getOnlineMembers(activeChar.getObjectId())) {
                    if (allmembers != activeChar) {
                        Skill skill = SkillTable.getInstance().getInfo(19009, 1);
                        for (EffectTemplate et : skill.getEffectTemplates()) {
                            Env env = new Env(allmembers, allmembers, skill);
                            Effect effect = et.getEffect(env);
                            effect.setPeriod(3600000);
                            allmembers.getEffectList().addEffect(effect);
                        }
                    }
                }
            Player clanLeader = activeChar.getClan().getLeader().getPlayer();
    		
    		if (clanLeader != null && !activeChar.isClanLeader() && clanLeader.isOnline() && activeChar.getClan().getLevel() >= 5)
    		{
                Skill skill = SkillTable.getInstance().getInfo(19009, 1);
                for (EffectTemplate et : skill.getEffectTemplates()) {
                    Env env = new Env(activeChar, activeChar, skill);
                    Effect effect = et.getEffect(env);
                    effect.setPeriod(3600000);
                    activeChar.getEffectList().addEffect(effect);
                }
    		}
        }

        // engage and notify Partner
        if (first && Config.ALLOW_WEDDING) {
            CoupleManager.getInstance().engage(activeChar);
            CoupleManager.getInstance().notifyPartner(activeChar);
        }

        if (first) {
            activeChar.getFriendList().notifyFriends(true);
            loadTutorial(activeChar);
            activeChar.restoreDisableSkills();
            activeChar.mentoringLoginConditions();
        }

        sendPacket(new L2FriendList(activeChar), new ExStorageMaxCount(activeChar), new QuestList(activeChar), new ExBasicActionList(activeChar), new EtcStatusUpdate(activeChar));

        activeChar.checkHpMessages(activeChar.getMaxHp(), activeChar.getCurrentHp());
        activeChar.checkDayNightMessages();

        if (Config.PETITIONING_ALLOWED)
            PetitionManager.getInstance().checkPetitionMessages(activeChar);

        if (!first) {
            if (activeChar.isCastingNow()) {
                Creature castingTarget = activeChar.getCastingTarget();
                Skill castingSkill = activeChar.getCastingSkill();
                long animationEndTime = activeChar.getAnimationEndTime();
                if (castingSkill != null && castingTarget != null && castingTarget.isCreature() && activeChar.getAnimationEndTime() > 0)
                    sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0));
            }

            if (activeChar.isInBoat())
                activeChar.sendPacket(activeChar.getBoat().getOnPacket(activeChar, activeChar.getInBoatPosition()));

            if (activeChar.isMoving || activeChar.isFollow)
                sendPacket(activeChar.movePacket());

            if (activeChar.getMountNpcId() != 0)
                sendPacket(new Ride(activeChar));

            if (activeChar.isFishing())
                activeChar.stopFishing();
        }

        activeChar.entering = false;
        activeChar.sendUserInfo(true);

        if (activeChar.isSitting())
            activeChar.sendPacket(new ChangeWaitType(activeChar, ChangeWaitType.WT_SITTING));
        if (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
            if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_BUY)
                sendPacket(new PrivateStoreMsgBuy(activeChar));
            else if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL || activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE)
                sendPacket(new PrivateStoreMsgSell(activeChar));
            else if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
                sendPacket(new RecipeShopMsg(activeChar));

        if (activeChar.isDead())
            sendPacket(new Die(activeChar));

        activeChar.unsetVar("offline");

        // на всякий случай
        activeChar.sendActionFailed();

        if (first && activeChar.isGM() && Config.SAVE_GM_EFFECTS && activeChar.getPlayerAccess().CanUseGMCommand) {
            // silence
            if (activeChar.getVarB("gm_silence")) {
                activeChar.setMessageRefusal(true);
                activeChar.sendPacket(SystemMsg.MESSAGE_REFUSAL_MODE);
            }
            // invul
            if (activeChar.getVarB("gm_invul")) {
                activeChar.setIsInvul(true);
                activeChar.startAbnormalEffect(AbnormalEffect.S_INVULNERABLE);
                activeChar.sendMessage(activeChar.getName() + " is now immortal.");
            }
            // gmspeed
            try {
                int var_gmspeed = Integer.parseInt(activeChar.getVar("gm_gmspeed"));
                if (var_gmspeed >= 1 && var_gmspeed <= 4)
                    activeChar.doCast(SkillTable.getInstance().getInfo(7029, var_gmspeed), activeChar, true);
            } catch (Exception E) {
            }
        }

        PlayerMessageStack.getInstance().CheckMessages(activeChar);

        sendPacket(ClientSetTime.STATIC, new ExSetCompassZoneCode(activeChar));

        Pair<Integer, OnAnswerListener> entry = activeChar.getAskListener(false);
        if (entry != null && entry.getValue() instanceof ReviveAnswerListener)
            sendPacket(new ConfirmDlg(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0).addString("Other player").addString("some"));

        if (activeChar.isCursedWeaponEquipped())
            CursedWeaponsManager.getInstance().showUsageTime(activeChar, activeChar.getCursedWeaponEquippedId());

        if (!first) {
            // Персонаж вылетел во время просмотра
            if (activeChar.isInObserverMode()) {
                if (activeChar.getObserverMode() == Player.OBSERVER_LEAVING)
                    activeChar.returnFromObserverMode();
                else if (activeChar.getOlympiadObserveGame() != null)
                    activeChar.leaveOlympiadObserverMode(true);
                else
                    activeChar.leaveObserverMode();
            } else if (activeChar.isVisible())
                World.showObjectsToPlayer(activeChar);

            for (Summon summon : activeChar.getPets())
                sendPacket(new PetInfo(summon));

            if (activeChar.isInParty()) {
                // sends new member party window for all members
                // we do all actions before adding member to a list, this speeds
                // things up a little
                sendPacket(new PartySmallWindowAll(activeChar.getParty(), activeChar));

                for (Player member : activeChar.getParty().getPartyMembers())
                    if (member != activeChar) {
                        sendPacket(new PartySpelled(member, true));
                        for (Summon memberPet : member.getPets())
                            sendPacket(new PartySpelled(memberPet, true));

                        sendPacket(RelationChanged.update(activeChar, member, activeChar));
                    }

                // Если партия уже в СС, то вновь прибывшем посылаем пакет
                // открытия окна СС
                if (activeChar.getParty().isInCommandChannel())
                    sendPacket(ExMPCCOpen.STATIC);
            }

            for (int shotId : activeChar.getAutoSoulShot())
                sendPacket(new ExAutoSoulShot(shotId, true));

            for (Effect e : activeChar.getEffectList().getAllFirstEffects())
                if (e.getSkill().isToggle())
                    sendPacket(new MagicSkillLaunched(activeChar.getObjectId(), e.getSkill().getId(), e.getSkill().getLevel(), activeChar));

            activeChar.broadcastCharInfo();
        } else
            activeChar.sendUserInfo(); // Отобразит права в клане

        activeChar.updateEffectIcons();
        activeChar.updateStats();

        if (Config.ALT_PCBANG_POINTS_ENABLED)
            activeChar.sendPacket(new ExPCCafePointInfo(activeChar, 0, 1, 2, 12));

        if (!activeChar.getPremiumItemList().isEmpty())
            activeChar.sendPacket(Config.GOODS_INVENTORY_ENABLED ? ExGoodsInventoryChangedNotify.STATIC : ExNotifyPremiumItem.STATIC);

        activeChar.sendVoteSystemInfo();
        activeChar.sendPacket(new ExReceiveShowPostFriend(activeChar));

        checkNewMail(activeChar);

        // GoD
        activeChar.sendPacket(new ExSubjobInfo(activeChar.getPlayer(), false));

        activeChar.sendPacket(new ExTutorialList());

        activeChar.sendPacket(new ExAcquirableSkillListByClass(activeChar));

        if (first)
            PartySubstitute.getInstance().addPlayerToParty(activeChar);

        if (activeChar.getOnlineTime() < 10)
        {
        	activeChar.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.GD1_INTRO));
        	//LoginServerCommunication.getInstance().sendPacket(new RequestIsLocIp(activeChar.getObjectId(), activeChar.getAccountName()));
        }
        activeChar.sendPacket(new ExChangeMPCost(1, -3.0));
        activeChar.sendPacket(new ExChangeMPCost(1, -5.0));
        activeChar.sendPacket(new ExChangeMPCost(0, 20.0));
        activeChar.sendPacket(new ExChangeMPCost(1, -10.0));
        activeChar.sendPacket(new ExChangeMPCost(3, -20.0));
        activeChar.sendPacket(new ExChangeMPCost(22, -20.0));

		if (activeChar.getClassId().isOfLevel(ClassLevel.Awaking))
			AwakingManager.getInstance().getRaceSkill(activeChar);

        if (activeChar.getLevel() >= 85 & activeChar.getVar("GermunkusUSM") == null & !activeChar.isAwaking())
            AwakingManager.getInstance().SendReqToStartQuest(activeChar);

    }

    private static void notifyClanMembers(Player activeChar) {
        Clan clan = activeChar.getClan();
        SubUnit subUnit = activeChar.getSubUnit();
        if (clan == null || subUnit == null)
            return;

        UnitMember member = subUnit.getUnitMember(activeChar.getObjectId());
        if (member == null)
            return;

        member.setPlayerInstance(activeChar, false);

        int sponsor = activeChar.getSponsor();
        int apprentice = activeChar.getApprentice();
        L2GameServerPacket msg = new SystemMessage2(SystemMsg.CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME).addName(activeChar);
        PledgeShowMemberListUpdate memberUpdate = new PledgeShowMemberListUpdate(activeChar);
        for (Player clanMember : clan.getOnlineMembers(activeChar.getObjectId())) {
            clanMember.sendPacket(memberUpdate);
            if (clanMember.getObjectId() == sponsor)
                clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_APPRENTICE_C1_HAS_LOGGED_OUT).addName(activeChar));
            else if (clanMember.getObjectId() == apprentice)
                clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_SPONSOR_C1_HAS_LOGGED_IN).addName(activeChar));
            else
                clanMember.sendPacket(msg);
        }

        if (!activeChar.isClanLeader())
            return;

        ClanHall clanHall = clan.getHasHideout() > 0 ? ResidenceHolder.getInstance().getResidence(ClanHall.class, clan.getHasHideout()) : null;
        if (clanHall == null || clanHall.getAuctionLength() != 0)
            return;

        if (clanHall.getSiegeEvent().getClass() != ClanHallAuctionEvent.class)
            return;

        if (clan.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) < clanHall.getRentalFee())
            activeChar.sendPacket(new SystemMessage2(SystemMsg.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_ME_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW).addLong(clanHall.getRentalFee()));
    }

    private void loadTutorial(Player player) {
        Quest q = QuestManager.getQuest(255);
        if (q != null)
            player.processQuestEvent(q.getName(), "UC", null);
    }

    private void checkNewMail(Player activeChar) {
        for (Mail mail : MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId()))
            if (mail.isUnread()) {
                sendPacket(ExNoticePostArrived.STATIC_FALSE);
                break;
            }
    }
}