package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.io.IOException;

public class ShortCoordinateBlockChunkWriter extends ObjectWriter<Block>
{
	public ShortCoordinateBlockChunkWriter(Writers writers)
	{
		super(writers, Block.class);
	}
	
	@Override
	protected void writeObject(Block block) throws IOException
	{
		final Chunk chunk = block.getChunk();
		
		final int x = block.getX() - chunk.getX() * 16;
		final int z = block.getZ() - chunk.getZ() * 16;
		final int y = block.getY();
		
		out.writeShort(x << 12 | z << 8 | y);
	}
}
