package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.service.BlockData;
import com.eul4.wrapper.BlockDataMap;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Map;

public class BlockDataMapWriter extends ObjectWriter<BlockDataMap>
{
	public BlockDataMapWriter(Writers writers)
	{
		super(writers, BlockDataMap.class);
	}
	
	@Override
	public void writeObject(BlockDataMap blockDataMap) throws IOException
	{
		out.writeInt(blockDataMap.size());
		
		for(Map.Entry<Block, BlockData> entry : blockDataMap.entrySet())
		{
			Block block = entry.getKey();
			BlockData blockData = entry.getValue();
			
			writers.getWriter(ShortCoordinateBlockChunkWriter.class).writeObject(block);
			writers.getWriter(BlockDataWriter.class).writeObject(blockData);
		}
	}
}
