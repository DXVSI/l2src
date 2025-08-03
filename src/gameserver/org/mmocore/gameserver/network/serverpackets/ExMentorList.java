package org.mmocore.gameserver.network.serverpackets;

import java.util.Collections;
import java.util.List;

import org.mmocore.gameserver.model.MenteeInfo;
import org.mmocore.gameserver.model.Player;

public class ExMentorList extends L2GameServerPacket {
	private List<MenteeInfo> _list = Collections.emptyList();
	private int _mentor;
	private Player activeChar;

	public ExMentorList(Player player) {
		_mentor = player.getMentorSystem().getMentor();
		_list = player.getMentorSystem().getMenteeInfo();
		activeChar = player;
	}

	@Override
	protected final void writeImpl() {
		writeEx(0x121);
		if (_mentor == 0 && activeChar.isMentor()) { // 02 приходит ученику, 01 - наставнику
			writeD(0x01);
		} else {
			writeD(0x02);
		}
		// writeD(_mentor == 0 ? 0x01 : 0x02);
		writeD(_list.size()); // Размер следующего списка
		for (MenteeInfo entry : _list) {
			writeD(entry.getObjectId()); // objectId
			writeS(entry.getName()); // nickname
			writeD(entry.getClassId());// classId
			writeD(entry.getLevel());// level
			writeD(entry.isOnline()); // online
		}
	}
}
