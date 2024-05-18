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
import lombok.Getter;

import java.io.IOException;

@Getter
public class GenericStructureReader extends ObjectReader<Structure>
{
	private final Reader<Structure> reader;
	private final ParameterizedReadable<Structure, Town> parameterizedReadable;
	
	public GenericStructureReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Structure.class);
		
		final ObjectType objectType = PluginObjectType.GENERIC_STRUCTURE;
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
	
	private void readerVersion0(Structure structure) throws IOException, ClassNotFoundException
	{
		getReaderAndWriteReference(structure, structure.getStructureType().getStructureClass());
	}
	
	@SuppressWarnings("unchecked")
	private <S extends Structure> void getReaderAndWriteReference(Structure structure, Class<S> type) throws IOException, ClassNotFoundException
	{
		((StructureReader<S>) readers.getReader(structure.getStructureType().getReaderClass())).readReference(type.cast(structure));
	}
	
	private Readable<Structure> parameterizedReadableVersion0(Town town)
	{
		return () -> (Structure) readers
				.getReader(StructureType.values()[in.readInt()].getReaderClass())
				.getParameterizedReadable()
				.getReadable(town)
				.read();
	}
	
	public Structure readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
