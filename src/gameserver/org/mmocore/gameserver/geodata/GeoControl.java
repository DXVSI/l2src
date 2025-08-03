package org.mmocore.gameserver.geodata;

import java.util.HashMap;

import org.mmocore.commons.geometry.Polygon;
import org.mmocore.gameserver.model.entity.Reflection;

public interface GeoControl
{
	public abstract Polygon getGeoPos();

	public abstract HashMap<Long, Byte> getGeoAround();

	public abstract void setGeoAround(HashMap<Long, Byte> value);

	public abstract Reflection getReflection();

	public abstract boolean isGeoCloser();
}