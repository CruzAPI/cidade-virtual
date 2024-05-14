package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.TownMap;

import java.io.IOException;

public class TownMapReader extends ObjectReader<TownMap>
{
	private final Reader<TownMap> reader;
	private final ParameterizedReadable<TownMap, Main> parameterizedReadable;
	
	public TownMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownMap.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_MAP;
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
	
	private Readable<TownMap> parameterizedReadableVersion0(Main plugin)
	{
		return () ->
		{
			TownMap towns = new TownMap();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				Town town = readers.getReader(TownReader.class).readReference(plugin);
				towns.put(town.getOwnerUUID(), town);
			}
			
			return towns;
		};
	}
	
	public TownMap readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected TownMap readObject(TownMap towns) throws IOException, ClassNotFoundException
	{
		return reader.readObject(towns);
	}
}
