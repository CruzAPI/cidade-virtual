package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.Generator;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public abstract class GeneratorReader<G extends Generator> extends StructureReader<G>
{
	private final Reader<G> reader;
	
	public GeneratorReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.GENERATOR;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private G readerVersion0(G generator) throws IOException
	{
		generator.setBalance(in.readInt());
		
		return generator;
	}
	
	@Override
	protected G readObject(G generator) throws IOException, ClassNotFoundException
	{
		super.readObject(generator);
		return reader.readObject(generator);
	}
}
