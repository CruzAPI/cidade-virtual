package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.UUID;

public class LocationReader extends ObjectReader<Location>
{
	@Getter
	private final Reader<Location> reader;
	private final ParameterizedReadable<Location, Plugin> parameterizedReadable;
	
	public LocationReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Location.class);
		
		final ObjectType objectType = CommonObjectType.LOCATION;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
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
}
