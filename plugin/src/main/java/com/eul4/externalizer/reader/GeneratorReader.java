package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.StructureStatus;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.model.town.structure.Generator;

import java.io.IOException;
import java.io.ObjectInput;

public abstract class GeneratorReader<G extends Generator> extends StructureReader<G>
{
	private final Reader<G> reader;
	
	public GeneratorReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getGeneratorVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Generator version: " + versions.getGeneratorVersion());
		}
	}
	
	private G readerVersion0(G generator) throws IOException, ClassNotFoundException
	{
		//TODO: read generator fields...
		
		return generator;
	}
	
	@Override
	protected G readObject(G generator) throws IOException, ClassNotFoundException
	{
		return reader.readObject(generator);
	}
}
