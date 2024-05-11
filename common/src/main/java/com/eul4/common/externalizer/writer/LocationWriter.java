package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.Location;

import java.io.IOException;

public class LocationWriter extends ObjectWriter<Location>
{
	public LocationWriter(Writers writers)
	{
		super(writers);
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
