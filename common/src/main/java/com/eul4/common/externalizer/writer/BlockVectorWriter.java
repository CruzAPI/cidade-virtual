package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.util.BlockVector;

import java.io.IOException;

public class BlockVectorWriter extends ObjectWriter<BlockVector>
{
	public BlockVectorWriter(Writers writers)
	{
		super(writers, BlockVector.class);
	}
	
	@Override
	protected void writeObject(BlockVector blockVector) throws IOException
	{
		out.writeInt(blockVector.getBlockX());
		out.writeInt(blockVector.getBlockY());
		out.writeInt(blockVector.getBlockZ());
	}
}
