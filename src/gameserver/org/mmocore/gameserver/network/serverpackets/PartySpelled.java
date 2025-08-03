package org.mmocore.gameserver.network.serverpackets;

import org.mmocore.gameserver.model.IconEffect;
import org.mmocore.gameserver.model.Playable;
import org.mmocore.gameserver.utils.EffectsComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartySpelled extends L2GameServerPacket implements IconEffectPacket
{
	private int _type;
	private int _objId;
	private List<IconEffect> _effects;

	public PartySpelled(Playable activeChar, boolean full)
	{
		_objId = activeChar.getObjectId();
		_type = activeChar.isPet() ? 1 : activeChar.isSummon() ? 2 : 0;
		// 0 - L2Player // 1 - петы // 2 - саммоны
		_effects = new ArrayList<IconEffect>();
		if (full)
		{
			org.mmocore.gameserver.model.Effect[] effects = activeChar.getEffectList().getAllFirstEffects();
			Arrays.sort(effects, EffectsComparator.getInstance());
			for (org.mmocore.gameserver.model.Effect effect : effects)
				if (effect != null && effect.isInUse())
					effect.addIcon(this);
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xf4);
		writeD(_type);
		writeD(_objId);
		writeD(_effects.size());
		for (IconEffect temp : _effects)
		{
			writeD(temp.getSkillId());
			writeH(temp.getLevel());
			writeD(temp.getDuration());
		}
	}

	@Override
	public void addIconEffect(int skillId, int level, int duration, int obj)
	{
		_effects.add(new IconEffect(skillId, level, duration, obj));
	}
}