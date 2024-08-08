package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.LocationReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.HomeMap;
import lombok.Getter;
import org.bukkit.Location;

import java.io.IOException;

public class HomeMapReader extends ObjectReader<HomeMap>
{
	@Getter
	private final Reader<HomeMap> reader;
	private final ParameterizedReadable<HomeMap, Main> parameterizedReadable;
	
	public HomeMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, HomeMap.class);
		
		final ObjectType objectType = PluginObjectType.HOME_MAP;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameteriedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private HomeMap readerVersion0(HomeMap homeMap) throws IOException, ClassNotFoundException
	{
		final int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			final String homeName = in.readUTF();
			final Location homeLocation = readers.getReader(LocationReader.class).readReference(homeMap.getPlugin());
			
			homeMap.put(homeName, homeLocation);
		}
		
		return homeMap;
	}
	
	private Readable<HomeMap> parameteriedReadableVersion0(Main plugin)
	{
		return () -> new HomeMap(plugin);
	}
	
	public HomeMap readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
