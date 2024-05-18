package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.Generator;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class GeneratorReader<G extends Generator> extends StructureReader<G>
{
	private final Reader<G> reader;
	
	public GeneratorReader(Readers readers, Class<G> type) throws InvalidVersionException
	{
		super(readers, type);
		
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
	
	private void readerVersion0(G generator) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(generator);
		
		generator.setBalance(in.readInt());
	}
}
