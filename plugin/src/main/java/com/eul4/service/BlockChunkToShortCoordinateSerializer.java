package com.eul4.service;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class BlockChunkToShortCoordinateSerializer
{
	public Block deserialize(Chunk chunk, short coordinates)
	{
		final int x = (coordinates >> 12) & 0x0F;
		final int z = (coordinates >> 8) & 0x0F;
		final int y = (coordinates) & 0xFF;
		
		return chunk.getBlock(x, y, z);
	}
	
	public short serialize(Block block)
	{
		final Chunk chunk = block.getChunk();
		
		final int x = block.getX() - chunk.getX() * 16;
		final int z = block.getZ() - chunk.getZ() * 16;
		final int y = block.getY();
		
		return (short) (x << 12 | z << 8 | y);
	}
}
