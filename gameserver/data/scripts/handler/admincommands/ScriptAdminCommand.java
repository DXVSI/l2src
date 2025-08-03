package handler.admincommands;

import org.mmocore.gameserver.handler.admincommands.AdminCommandHandler;
import org.mmocore.gameserver.handler.admincommands.IAdminCommandHandler;
import org.mmocore.gameserver.scripts.ScriptFile;

/**
 * @author VISTALL
 * @date 22:57/08.04.2011
 */
public abstract class ScriptAdminCommand implements IAdminCommandHandler, ScriptFile
{
	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{

	}

	@Override
	public void onShutdown()
	{

	}
}
