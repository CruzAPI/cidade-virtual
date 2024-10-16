package com.eul4.externalizer.reader;

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
import com.eul4.wrapper.StructureMap;
import lombok.Getter;

import java.io.IOException;

@Getter
public class StructureMapReader extends ObjectReader<StructureMap>
{
	private final Reader<StructureMap> reader;
	private final ParameterizedReadable<StructureMap, Town> parameterizedReadable;
	
	public StructureMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, StructureMap.class);
		
		final ObjectType objectType = PluginObjectType.STRUCTURE_MAP;
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
	
	private Readable<StructureMap> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			StructureMap structureMap = new StructureMap(town.getOwnerUniqueId());
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				Structure structure = readers.getReader(GenericStructureReader.class).readReference(town);
				structureMap.put(structure.getUUID(), structure);
			}
			
			return structureMap;
		};
	}
	
	public StructureMap readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
