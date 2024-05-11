package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownBlock;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Map;

public class TownBlockMapWriter extends ObjectWriter<Map<Block, TownBlock>>
{
	public TownBlockMapWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Map<Block, TownBlock> townBlockMap) throws IOException
	{
		//TODO: write fields...
	}
}
