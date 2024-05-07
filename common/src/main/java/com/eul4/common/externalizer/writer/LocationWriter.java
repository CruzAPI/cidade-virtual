package com.eul4.common.externalizer.writer;

import org.bukkit.Location;

import java.io.IOException;
import java.io.ObjectOutput;

public class LocationWriter extends ObjectWriter<Location>
{
	public LocationWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(Location location) throws IOException
	{
		out.writeObject(location.getWorld().getUID());
		
		out.writeDouble(location.getX());
		out.writeDouble(location.getY());
		out.writeDouble(location.getZ());
		
		out.writeFloat(location.getYaw());
		out.writeFloat(location.getPitch());
	}
}
