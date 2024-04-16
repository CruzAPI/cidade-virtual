package com.eul4.externalizer;

import com.eul4.Main;
import com.eul4.service.BlockData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BlockDataMapExternalizer
{
	private final Main plugin;
	
	public void write(Map<Block, BlockData> blockDataMap, DataOutput out) throws IOException
	{
		out.writeLong(BlockData.SERIAL_VERSION_UID);
		out.writeInt(blockDataMap.size());
		
		for(Map.Entry<Block, BlockData> entry : blockDataMap.entrySet())
		{
			final Block block = entry.getKey();
			final BlockData blockData = entry.getValue();
			
			plugin.getBlockChunkToShortCoordinateExternalizer().write(block, out);
			plugin.getBlockDataExternalizer().write(blockData, out);
		}
	}
	
	public Map<Block, BlockData> read(Chunk chunk, DataInput in) throws IOException
	{
		long version = in.readLong();
		int size = in.readInt();
		
		Map<Block, BlockData> blockDataMap = new HashMap<>();
		
		for(int i = 0; i < size; i++)
		{
			final Block block = plugin.getBlockChunkToShortCoordinateExternalizer().read(chunk, in);
			final BlockData blockData = plugin.getBlockDataExternalizer().read(version, in);
			
			blockDataMap.put(block, blockData);
		}
		
		return blockDataMap;
	}
}
