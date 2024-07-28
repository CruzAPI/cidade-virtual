package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Structure;
import com.eul4.wrapper.StructureMap;

import java.io.IOException;

public class StructureMapWriter extends ObjectWriter<StructureMap>
{
	public StructureMapWriter(Writers writers)
	{
		super(writers, StructureMap.class);
	}
	
	@Override
	protected void writeObject(StructureMap structureMap) throws IOException
	{
		out.writeInt(structureMap.size());
		
		for(Structure structure : structureMap.values())
		{
			writers.getWriter(GenericStructureWriter.class).writeReference(structure);
		}
	}
}
