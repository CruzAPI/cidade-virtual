package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.StructureSet;

import java.io.IOException;

public class StructureSetReader extends ObjectReader<StructureSet>
{
	private final Reader<StructureSet> reader;
	private final ParameterizedReadable<StructureSet, Town> parameterizedReadable;
	
	public StructureSetReader(Readers readers) throws InvalidVersionException
	{
		super(readers, StructureSet.class);
		
		final ObjectType objectType = PluginObjectType.STRUCTURE_SET;
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
	
	private Readable<StructureSet> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			StructureSet structureSet = new StructureSet();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				structureSet.add(readers.getReader(GenericStructureReader.class).readReference(town));
			}
			
			return structureSet;
		};
	}
	
	public StructureSet readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected StructureSet readObject(StructureSet structureSet) throws IOException, ClassNotFoundException
	{
		return reader.readObject(structureSet);
	}
}
