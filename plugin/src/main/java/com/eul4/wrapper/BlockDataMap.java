package com.eul4.wrapper;

import com.eul4.service.BlockData;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class BlockDataMap
{
	private final Chunk chunk;
	private final LinkedHashMap<Block, BlockData> map;
	
	public BlockDataMap(Chunk chunk)
	{
		this(chunk, new LinkedHashMap<>());
	}
	
	public BlockDataMap(Chunk chunk, LinkedHashMap<Block, BlockData> map)
	{
		this.chunk = chunk;
		this.map = map;
	}
	
	public BlockData get(Block block)
	{
		return this.map.get(block);
	}
	
	public BlockData remove(Block block)
	{
		return this.map.remove(block);
	}
	
	public boolean containsKey(Block block)
	{
		return this.map.containsKey(block);
	}
	
	public BlockData put(Block block, BlockData blockData)
	{
		return this.map.put(block, blockData);
	}
	
	public Set<Map.Entry<Block, BlockData>> entrySet()
	{
		return this.map.entrySet();
	}
	
	public int size()
	{
		return this.map.size();
	}
}
