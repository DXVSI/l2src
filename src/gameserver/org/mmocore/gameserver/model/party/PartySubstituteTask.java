package org.mmocore.gameserver.model.party;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Request;
import org.mmocore.gameserver.model.Request.L2RequestType;
import org.mmocore.gameserver.network.serverpackets.ExRegistPartySubstitute;
import org.mmocore.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class PartySubstituteTask extends RunnableImpl
{
	@Override
	public void runImpl() throws Exception
	{
		ConcurrentMap<Player, Integer> _wPlayers = PartySubstitute.getInstance().getWaitingPlayer();
		Set<Player> _wPartys = PartySubstitute.getInstance().getWaitingParty();

		Set<Entry<Player, Integer>> sets = _wPlayers.entrySet();

		for (Entry<Player, Integer> e : sets)
		{
			Player p = e.getKey();

			if (e.getValue() > 4)
			{
				PartySubstitute.getInstance().removePlayerReplace(p);
				p.getParty().getPartyLeader().sendPacket(new ExRegistPartySubstitute(p.getObjectId(), ExRegistPartySubstitute.REGISTER_TIMEOUT));
				continue;
			}

			for (Player pp : _wPartys)
			{
				if (PartySubstitute.getInstance().isGoodPlayer(p, pp))
				{
					if (pp.isProcessingRequest())
						continue;

					new Request(L2RequestType.SUBSTITUTE, p, pp).setTimeout(10000L);
					pp.sendPacket(new ExRegistWaitingSubstituteOk(p.getParty(), p));

					break;
				}
			}
			PartySubstitute.getInstance().updatePlayerToReplace(p, e.getValue() + 1);
		}
	}

}
