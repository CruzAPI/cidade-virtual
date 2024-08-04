package com.eul4.common.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class EntityUtil
{
	public static void targetEye(LivingEntity entity, LivingEntity target)
	{
		target(entity, entity.getEyeLocation(), target.getEyeLocation());
	}
	
	public static void target(Entity entity, Location entityLocation, Location targetLocation)
	{
		if(entityLocation.getWorld() != targetLocation.getWorld())
		{
			entity.setRotation(0.0F, 0.0F);
		}
		
		final Vector direction = targetLocation.toVector().subtract(entityLocation.toVector());
		
		final float yaw = (float) Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
		final float pitch = (float) Math.toDegrees(Math.atan2(-direction.getY(), direction.length()));
		
		entity.setRotation(yaw, pitch);
	}
}
