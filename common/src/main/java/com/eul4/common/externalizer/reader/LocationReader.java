package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.UUID;

public class LocationReader extends ObjectReader<Location>
{
	private final Reader<Location> reader;
	private final ParameterizedReadable<Location, Plugin> parameterizedReadable;
	
	public LocationReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getLocationVersion() == 0)
		{
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Location version: " + commonVersions.getLocationVersion());
		}
	}
	
	private Readable<Location> parameterizedReadableVersion0(Plugin plugin)
	{
		return () ->
		{
			UUID uuid = new UUID(in.readLong(), in.readLong());
			
			double x = in.readDouble();
			double y = in.readDouble();
			double z = in.readDouble();
			
			float yaw = in.readFloat();
			float pitch = in.readFloat();
			
			return new Location(plugin.getServer().getWorld(uuid), x, y, z, yaw, pitch);
		};
	}
	
	public Location readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected Location readObject(Location location) throws IOException, ClassNotFoundException
	{
		return reader.readObject(location);
	}
}
