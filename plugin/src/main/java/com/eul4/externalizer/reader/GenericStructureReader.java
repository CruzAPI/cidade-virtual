package com.eul4.externalizer.reader;

import com.eul4.StructureType;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public class GenericStructureReader extends ObjectReader<Structure>
{
	private final Reader<Structure> reader;
	private final ParameterizedReadable<Structure, Town> parameterizedReadable;
	
	public GenericStructureReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.GENERIC_STRUCTURE;
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
	
	private Readable<Structure> parameterizedReadableVersion0(Town town)
	{
		return () -> readers
				.getReader(StructureType.values()[in.readInt()].getReaderClass())
				.readReference(town);
	}
	
	public Structure readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected Structure readObject(Structure structure) throws IOException, ClassNotFoundException
	{
		return reader.readObject(structure);
	}
}
