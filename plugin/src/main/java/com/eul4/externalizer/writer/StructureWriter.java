package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;

public abstract class StructureWriter<S extends Structure> extends ObjectWriter<S>
{
	public StructureWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(S structure) throws IOException
	{
		//TODO: write fields...
		
//		out.writeObject(uuid);
//		out.writeObject(new BlockSerializable(centerTownBlock.getBlock()));
//		out.writeInt(level);
//		out.writeInt(rotation);
//		townSerializer.writeStructureTownBlocks(townBlocks, out);
//		out.writeInt(status.ordinal());
//		out.writeInt(buildTicks);
//		out.writeInt(totalBuildTicks);
//		out.writeObject(hologram);
	}
}
