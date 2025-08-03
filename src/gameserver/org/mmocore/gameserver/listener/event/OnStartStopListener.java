package org.mmocore.gameserver.listener.event;

import org.mmocore.gameserver.listener.EventListener;
import org.mmocore.gameserver.model.entity.events.GlobalEvent;

/**
 * @author VISTALL
 * @date 7:18/10.06.2011
 */
public interface OnStartStopListener extends EventListener
{
	void onStart(GlobalEvent event);

	void onStop(GlobalEvent event);
}
