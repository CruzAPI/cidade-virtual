package com.eul4.common.wrapper;

import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.Server;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode
public class LocationSerializable implements Serializable
{
	private transient Location location;
	
	private final UUID worldUid;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	
	public LocationSerializable(Location location)
	{
		this.location = location;
		
		worldUid = location.getWorld().getUID();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}
	
	public Location getBukkitLocation(Server server)
	{
		return (location == null ? location = new Location(server.getWorld(worldUid), x, y, z, yaw, pitch) : location).clone();
	}
}