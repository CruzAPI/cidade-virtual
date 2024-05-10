package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TownMapReader extends ObjectReader<Map<UUID, Town>>
{
	private final Reader<Map<UUID, Town>> reader;
	private final ParameterizedReadable<Map<UUID, Town>, Main> parameterizedReadable;
	
	private final TownReader townReader;
	
	public TownMapReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townReader = new TownReader(in, versions);
		
		if(versions.getTownMapVersion() == 0)
		{
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownMap version: " + versions.getTownMapVersion());
		}
	}
	
	private Readable<Map<UUID, Town>> parameterizedReadableVersion0(Main plugin) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			Map<UUID, Town> towns = new HashMap<>();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				Town town = townReader.readReference(plugin);
				towns.put(town.getOwnerUUID(), town);
			}
			
			return towns;
		};
	}
	
	public Map<UUID, Town> readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected Map<UUID, Town> readObject(Map<UUID, Town> towns) throws IOException, ClassNotFoundException
	{
		return reader.readObject(towns);
	}
}
