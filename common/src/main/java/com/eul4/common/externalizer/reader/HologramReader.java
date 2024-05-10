package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;

import java.io.IOException;
import java.io.ObjectInput;

public class HologramReader extends ObjectReader<Hologram>
{
	private final Reader<Hologram> reader;
	private final ParameterizedReadable<Hologram, Common> parameterizedReadable;
	
	private final TranslatedHologramLineReader translatedHologramLineReader;
	private final LocationReader locationReader;
	
	public HologramReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.locationReader = new LocationReader(in, commonVersions);
		this.translatedHologramLineReader = new TranslatedHologramLineReader(in, commonVersions);
		
		if(commonVersions.getHologramVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Hologram version: " + commonVersions.getHologramVersion());
		}
	}
	
	private Readable<Hologram> parameterizedReadableVersion0(Common plugin) throws IOException, ClassNotFoundException
	{
		return () -> new Hologram(plugin, locationReader.readReference(plugin));
	}
	
	private Hologram readerVersion0(Hologram hologram) throws IOException, ClassNotFoundException
	{
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			hologram.getHologramLines().add(translatedHologramLineReader.readReference(hologram));
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
