package com.eul4.externalizer.reader;

import com.eul4.StructureType;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public class GenericStructureReader extends ObjectReader<Structure>
{
	private final Reader<Structure> reader;
	private final ParameterizedReadable<Structure, Town> parameterizedReadable;
	
	private final Map<StructureType, StructureReader<?>> readers = new HashMap<>();
	
	public GenericStructureReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		for(StructureType type : StructureType.values())
		{
			readers.put(type, type.getStructureReaderConstructor().newInstance(in, versions));
		}
		
		if(versions.getGenericPluginPlayerVersion() == 0)
		{
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid GenericPluginPlayer version: " + versions.getGenericPluginPlayerVersion());
		}
	}
	
	private Readable<Structure> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> readers.get(StructureType.values()[in.readInt()]).readReference(town);
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
