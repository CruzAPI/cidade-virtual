package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.StructureStatus;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashSet;
import java.util.Set;

public class StructureSetReader extends ObjectReader<Set<Structure>>
{
	private final Reader<Set<Structure>> reader;
	private final ParameterizedReadable<Set<Structure>, Town> parameterizedReadable;
	
	protected final GenericStructureReader genericStructureReader;
	
	public StructureSetReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.genericStructureReader = new GenericStructureReader(in, versions);
		
		if(versions.getStructureVersion() == 0)
		{
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Structure version: " + versions.getStructureVersion());
		}
	}
	
	private Readable<Set<Structure>> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			Set<Structure> structureSet = new HashSet<>();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				structureSet.add(genericStructureReader.readReference(town));
			}
			
			return structureSet;
		};
	}
	
	public Set<Structure> readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected Set<Structure> readObject(Set<Structure> structureSet) throws IOException, ClassNotFoundException
	{
		return reader.readObject(structureSet);
	}
}
