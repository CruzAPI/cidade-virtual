package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownBlock;

import java.io.IOException;

public class TownBlockWriter extends ObjectWriter<TownBlock>
{
	public TownBlockWriter(Writers writers)
	{
		super(writers, TownBlock.class);
	}
	
	@Override
	protected void writeObject(TownBlock townBlock) throws IOException
	{
		final int absolutX = townBlock.getBlock().getX();
		final int absolutZ = townBlock.getBlock().getZ();
		
		final int relativeX = absolutX - townBlock.getTown().getBlock().getX();
		final int relativeZ = absolutZ - townBlock.getTown().getBlock().getZ();
		
		final int position = (relativeZ + 55) * 111 + (relativeX + 55);
		
		out.writeShort(position);
		out.writeBoolean(townBlock.isAvailable());
		writers.getWriter(GenericStructureWriter.class).writeReference(townBlock.getStructure());
	}
}
