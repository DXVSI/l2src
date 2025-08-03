package org.mmocore.gameserver.skills.effects;

import java.util.concurrent.Future;

import org.mmocore.commons.threading.RunnableImpl;
import org.mmocore.gameserver.ThreadPoolManager;
import org.mmocore.gameserver.model.Effect;
import org.mmocore.gameserver.model.Player;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.stats.Env;
import org.mmocore.gameserver.tables.SkillTable;

public final class EffectClanBirth extends Effect
{
	private Future<?> _task = null;
	public EffectClanBirth(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ActionTask(), 600000L, 600000L);
	}
	
	private class ActionTask extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			Player player = _effected != null && _effected.isPlayer() ? (Player) _effected : null;
			if (player == null || _effected.getClan() == null)
				return;

			Player clanLeader = _effected.getClan().getLeader().getPlayer();
			if (clanLeader != null && clanLeader.isOnline())
			{
				long onlineTime = (System.currentTimeMillis() - clanLeader.getOnlineBeginTime()) / 1000;
				if (onlineTime > 600)
				{
					Skill skill = SkillTable.getInstance().getInfo(19009, getLevel() == 5 ? 5 : getLevel() + 1);
					for (EffectTemplate et : skill.getEffectTemplates())
					{
						Env env = new Env(_effected, _effected, skill);
						Effect effect = et.getEffect(env);
						effect.setPeriod(3600000);
						_effected.getEffectList().addEffect(effect);
			        }
				}
			} else {
				_task.cancel(true);
				_task = null;
			}
		}
	}

	@Override
	public void onExit()
	{
		super.onExit();		
		_task.cancel(true);
		_task = null;
	}

	@Override
	public boolean onActionTime()
	{	
		return false;
	}
}