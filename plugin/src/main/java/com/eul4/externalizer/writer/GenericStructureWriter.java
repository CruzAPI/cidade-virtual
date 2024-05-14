package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;

public class GenericStructureWriter extends ObjectWriter<Structure>
{
	public GenericStructureWriter(Writers writers)
	{
		super(writers, Structure.class);
	}
	
	@Override
	protected void writeObject(Structure structure) throws IOException
	{
		out.writeInt(structure.getStructureType().ordinal());
		getWriterAndWriteReference(structure, structure.getStructureType().getStructureClass());
	}
	
	@SuppressWarnings("unchecked")
	private <S extends Structure> void getWriterAndWriteReference(Structure structure, Class<S> type) throws IOException
	{
		StructureWriter<S> writer = (StructureWriter<S>) writers.getWriter(structure.getStructureType().getWriterClass());
		
		writer.writeReference(type.cast(structure));
	}
}
