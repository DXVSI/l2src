package quests;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.instancemanager.ServerVariables;
import org.mmocore.gameserver.listener.actor.OnDeathListener;
import org.mmocore.gameserver.model.party.Party;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.GameObjectsStorage;
import org.mmocore.gameserver.model.instances.NpcInstance;
import org.mmocore.gameserver.model.quest.Quest;
import org.mmocore.gameserver.model.quest.QuestState;
import org.mmocore.gameserver.scripts.ScriptFile;

/**
* @author cruel
* @name 498 - Налетчик измерений Феллине
* @category Daily quest. Solo
* @see http://l2on.net/?c=quests&id=498
*/
public class _498_FelliniRaiderMeasurements extends Quest implements ScriptFile
{
	private static final int KARTIA_RESEARCHER = 33647;
	private static final int KARTIA_RB = 25883;
	private static final int KARTIA_BOX = 34931;
	private static final String KARTIA_KILL = "KartiaRb";

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _498_FelliniRaiderMeasurements()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCHER);
		addTalkId(KARTIA_RESEARCHER);
		addKillId(KARTIA_RB);
		addKillNpcWithLog(1, KARTIA_KILL, 1, KARTIA_RB);
		addLevelCheck(90, 94);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("33647-07.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("33647-10.htm"))
		{
			st.giveItems(KARTIA_BOX, 1);
			st.unset(KARTIA_KILL);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}	
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if(npcId == KARTIA_RESEARCHER)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 90 && st.getPlayer().getLevel() < 95)
				{
					if (st.isNowAvailableByTime())
						htmltext = "33647-01.htm";
					else
						htmltext = "33647-03.htm";
				}
				else
				{
					htmltext = "33647-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "33647-08.htm";
			else if(cond == 2)
				htmltext = "33647-09.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		boolean doneKill = updateKill(npc, st);
		Party party = st.getPlayer().getParty();
		if(party != null) {
			for(Player member : party.getPartyMembers())
			{
				QuestState qs = member.getQuestState(getClass());
				if(qs != null && qs.isStarted())
				{
					if(qs.getCond() == 1 && npc.getNpcId() == KARTIA_RB && doneKill)
						qs.setCond(2);
				}
			}		
		} else 
		{
			if(cond == 1 && npc.getNpcId() == KARTIA_RB)
			{
				if (doneKill)
					st.setCond(2);
			}
		}
		return null;
	}
}