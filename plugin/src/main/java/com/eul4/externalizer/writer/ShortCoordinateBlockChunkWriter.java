package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.awt.*;
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
		
		writers.getWriter(Point4BitWriter.class).writeObject(new Point(x, z));
		
		final int y = block.getY();
		
		out.writeShort(y);
	}
}
