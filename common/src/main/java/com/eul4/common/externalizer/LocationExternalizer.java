package com.eul4.common.externalizer;

import com.eul4.common.Common;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.wrapper.LocationSerializable;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

@RequiredArgsConstructor
public class LocationExternalizer
{
	private final Common plugin;
	
	public static final long VERSION = 0L;
	
	public Location read(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readLong(), in);
	}
	
	public Location read(long version, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			UUID uuid = (UUID) in.readObject();
			
			double x = in.readDouble();
			double y = in.readDouble();
			double z = in.readDouble();
			
			float yaw = in.readFloat();
			float pitch = in.readFloat();
			
			return new Location(plugin.getServer().getWorld(uuid), x, y, z, yaw, pitch);
		}
		
		throw new RuntimeException();
	}
	
	public void write(Location location, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		
		out.writeObject(location.getWorld().getUID());
		
		out.writeDouble(location.getX());
		out.writeDouble(location.getY());
		out.writeDouble(location.getZ());
		
		out.writeFloat(location.getYaw());
		out.writeFloat(location.getPitch());
	}
}
