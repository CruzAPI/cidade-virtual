package com.eul4.common.wrapper;

import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
public class VectorSerializable implements Serializable
{
	private transient Vector vector;
	
	private final double x;
	private final double y;
	private final double z;
	
	public VectorSerializable(Vector vector)
	{
		x = vector.getX();
		y = vector.getY();
		z = vector.getZ();
	}
	
	public Vector getBukkitVector()
	{
		return (vector == null ? vector = new Vector(x, y, z) : vector).clone();
	}
}