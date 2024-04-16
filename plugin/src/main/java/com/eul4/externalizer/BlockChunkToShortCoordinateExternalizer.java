package com.eul4.externalizer;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BlockChunkToShortCoordinateExternalizer
{
	public Block read(Chunk chunk, DataInput in) throws IOException
	{
		short coordinates = in.readShort();
		
		final int x = (coordinates >> 12) & 0x0F;
		final int z = (coordinates >> 8) & 0x0F;
		final int y = (coordinates) & 0xFF;
		
		return chunk.getBlock(x, y, z);
	}
	
	public void write(Block block, DataOutput out) throws IOException
	{
		final Chunk chunk = block.getChunk();
		
		final int x = block.getX() - chunk.getX() * 16;
		final int z = block.getZ() - chunk.getZ() * 16;
		final int y = block.getY();
		
		out.writeShort(x << 12 | z << 8 | y);
	}
}
