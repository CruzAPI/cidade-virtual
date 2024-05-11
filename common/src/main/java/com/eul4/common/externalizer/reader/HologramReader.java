package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;

import java.io.IOException;

public class HologramReader extends ObjectReader<Hologram>
{
	private final Reader<Hologram> reader;
	private final ParameterizedReadable<Hologram, Common> parameterizedReadable;
	
	public HologramReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = CommonObjectType.HOLOGRAM;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Hologram> parameterizedReadableVersion0(Common plugin)
	{
		return () -> new Hologram(plugin,
				readers.getReader(LocationReader.class).readReference(plugin));
	}
	
	private Hologram readerVersion0(Hologram hologram) throws IOException, ClassNotFoundException
	{
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			hologram.getHologramLines().add(readers.getReader(TranslatedHologramLineReader.class).readReference(hologram));
		}
		
		return hologram;
	}
	
	public Hologram readReference(Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected Hologram readObject(Hologram hologram) throws IOException, ClassNotFoundException
	{
		return reader.readObject(hologram);
	}
}
