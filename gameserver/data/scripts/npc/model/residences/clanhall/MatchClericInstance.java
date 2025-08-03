package npc.model.residences.clanhall;

import org.mmocore.commons.util.Rnd;
import org.mmocore.gameserver.model.Creature;
import org.mmocore.gameserver.model.Skill;
import org.mmocore.gameserver.model.instances.residences.clanhall.CTBBossInstance;
import org.mmocore.gameserver.templates.npc.NpcTemplate;

import ai.residences.clanhall.MatchCleric;

/**
 * @author VISTALL
 * @date 19:48/22.04.2011
 */
public class MatchClericInstance extends CTBBossInstance
{
	/**
	* 
	*/
	private static final long serialVersionUID = 5519166565714471914L;
	private long _massiveDamage;

	public MatchClericInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if(_massiveDamage > System.currentTimeMillis())
		{
			damage = 10000;
			if(Rnd.chance(10))
				((MatchCleric) getAI()).heal();
		}
		else if(getCurrentHpPercents() > 50)
		{
			if(attacker.isPlayer())
				damage = damage / getMaxHp() / 0.05 * 100;
			else
				damage = damage / getMaxHp() / 0.05 * 10;
		}
		else if(getCurrentHpPercents() > 30)
		{
			if(Rnd.chance(90))
			{
				if(attacker.isPlayer())
					damage = damage / getMaxHp() / 0.05 * 100;
				else
					damage = damage / getMaxHp() / 0.05 * 10;
			}
			else
				_massiveDamage = System.currentTimeMillis() + 5000L;
		}
		else
			_massiveDamage = System.currentTimeMillis() + 5000L;

		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
