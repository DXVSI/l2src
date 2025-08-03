package instances;

import org.mmocore.gameserver.model.entity.Reflection;
import org.mmocore.gameserver.utils.Location;

public class TeredorCavern extends Reflection
{
	private static int Teredor = 25785;
	private static Location TeredorSpawnCoords= new Location (176160, -185200, -3800);

	@Override
	protected void onCreate()
	{
		super.onCreate();
		addSpawnWithoutRespawn(Teredor, TeredorSpawnCoords, 0);
	}
}