package org.mmocore.gameserver.network.clientpackets;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 02.07.12 Time: 0:48 Приходит при нажатии наставником кнопки "+" в окне учеников Ответом на пакет
 * является {@link org.mmocore.gameserver.network.serverpackets.ListMenteeWaiting}
 */
public class RequestMenteeWaitingList extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		readD();// unk, always 1
		readD();// min level?
		readD();// max level
	}

	@Override
	protected void runImpl() throws Exception
	{
	}
}
