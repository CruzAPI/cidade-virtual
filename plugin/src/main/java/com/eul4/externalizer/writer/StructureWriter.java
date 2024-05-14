package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.HologramWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;

public abstract class StructureWriter<S extends Structure> extends ObjectWriter<S>
{
	public StructureWriter(Writers writers, Class<S> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(S structure) throws IOException
	{
		writers.getWriter(TownBlockWriter.class).writeReference(structure.getCenterTownBlock());
		out.writeInt(structure.getLevel());
		out.writeInt(structure.getRotation());
		writers.getWriter(TownBlockSetWriter.class).writeReference(structure.getTownBlockSet());
		out.writeInt(structure.getStatus().ordinal());
		out.writeInt(structure.getBuildTicks());
		out.writeInt(structure.getTotalBuildTicks());
		writers.getWriter(HologramWriter.class).writeReference(structure.getHologram());
	}
}
