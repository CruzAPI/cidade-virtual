package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.service.BlockData;

import java.io.IOException;

public class BlockDataWriter extends ObjectWriter<BlockData>
{
	public BlockDataWriter(Writers writers)
	{
		super(writers, BlockData.class);
	}
	
	@Override
	protected void writeObject(BlockData blockData) throws IOException
	{
		out.writeBoolean(blockData.hasHardness());
		out.writeByte(blockData.getRarity().getId());
		out.writeFloat(blockData.getHealth());
		out.writeBoolean(blockData.willDrop());
		out.write(blockData.enchantments);
		out.writeByte(blockData.getOrigin().getId());
		writers.getWriter(StabilityFormulaWriter.class).writeReferenceNotNull(blockData.getStabilityFormula());
		out.writeByte(blockData.getScrapeHealth());
	}
}
