package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import org.bukkit.Location;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.UUID;

public class LocationReader extends ObjectReader<Location>
{
	private final Common plugin;
	
	private final Reader<Location> reader;
	
	public LocationReader(ObjectInput in, CommonVersions commonVersions, Common plugin) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.plugin = plugin;
		
		if(commonVersions.getLocationVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Location version: " + commonVersions.getItemStackVersion());
		}
	}
	
	private Location readerVersion0() throws IOException, ClassNotFoundException
	{
		UUID uuid = (UUID) in.readObject();
		
		double x = in.readDouble();
		double y = in.readDouble();
		double z = in.readDouble();
		
		float yaw = in.readFloat();
		float pitch = in.readFloat();
		
		return new Location(plugin.getServer().getWorld(uuid), x, y, z, yaw, pitch);
	}
	
	@Override
	protected Location readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
