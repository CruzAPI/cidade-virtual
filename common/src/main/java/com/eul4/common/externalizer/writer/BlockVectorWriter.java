package com.eul4.common.externalizer.writer;

import org.bukkit.util.BlockVector;

import java.io.IOException;
import java.io.ObjectOutput;

public class BlockVectorWriter extends ObjectWriter<BlockVector>
{
	public BlockVectorWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(BlockVector blockVector) throws IOException
	{
		out.writeInt(blockVector.getBlockX());
		out.writeInt(blockVector.getBlockY());
		out.writeInt(blockVector.getBlockZ());
	}
}
