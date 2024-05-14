package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Structure;
import com.eul4.wrapper.StructureSet;

import java.io.IOException;

public class StructureSetWriter extends ObjectWriter<StructureSet>
{
	public StructureSetWriter(Writers writers)
	{
		super(writers, StructureSet.class);
	}
	
	@Override
	protected void writeObject(StructureSet structureSet) throws IOException
	{
		out.writeInt(structureSet.size());
		
		for(Structure structure : structureSet)
		{
			writers.getWriter(GenericStructureWriter.class).writeReference(structure);
		}
	}
}
