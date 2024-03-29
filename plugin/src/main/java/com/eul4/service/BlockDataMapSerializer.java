package com.eul4.service;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BlockDataMapSerializer
{
	private final Main plugin;
	
	public Map<Short, byte[]> serialize(Map<Block, BlockData> blockDataMap)
	{
		Map<Short, byte[]> serializedBlockDataMap = new HashMap<>();
		
		for(Map.Entry<Block, BlockData> entry : blockDataMap.entrySet())
		{
			final Block block = entry.getKey();
			final BlockData blockData = entry.getValue();
			
			final short coordinates = plugin.getBlockChunkToShortCoordinateSerializer().serialize(block);
			final byte[] serializedBlockData = plugin.getBlockDataSerializer().serialize(blockData);
			
			serializedBlockDataMap.put(coordinates, serializedBlockData);
		}
		
		return serializedBlockDataMap;
	}
	
	public Map<Block, BlockData> deserialize(Map<Short, byte[]> serializedBlockDataMap, Chunk chunk)
	{
		final Map<Block, BlockData> blockDataMap = new HashMap<>();
		
		for(Map.Entry<Short, byte[]> entry : serializedBlockDataMap.entrySet())
		{
			final short coordinates = entry.getKey();
			final byte[] serializedBlockData = entry.getValue();
			
			final Block block = plugin.getBlockChunkToShortCoordinateSerializer().deserialize(chunk, coordinates);
			final BlockData blockData = plugin.getBlockDataSerializer().deserialize(serializedBlockData);
			
			blockDataMap.put(block, blockData);
		}
		
		return blockDataMap;
	}
}
