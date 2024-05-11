package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import org.bukkit.block.Structure;

import java.io.IOException;
import java.util.Set;

public class StructureSetWriter extends ObjectWriter<Set<Structure>>
{
	public StructureSetWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Set<Structure> structureSet) throws IOException
	{
		//TODO: write fields...
	}
}
