package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.data.xml.holder.PetitionGroupHolder;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.petition.PetitionMainGroup;
import org.mmocore.gameserver.utils.Language;

import java.util.Collection;

/**
 * @author VISTALL
 */
public class ExResponseShowStepOne extends L2GameServerPacket
{
	private Language _language;

	public ExResponseShowStepOne(Player player)
	{
		_language = player.getLanguage();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xAF);
		Collection<PetitionMainGroup> petitionGroups = PetitionGroupHolder.getInstance().getPetitionGroups();
		writeD(petitionGroups.size());
		for (PetitionMainGroup group : petitionGroups)
		{
			writeC(group.getId());
			writeS(group.getName(_language));
		}
	}
}