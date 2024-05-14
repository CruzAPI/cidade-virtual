package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.externalizer.reader.TownBlockReader;
import com.eul4.model.town.TownBlock;
import com.eul4.wrapper.TownBlockMap;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Map;

public class TownBlockMapWriter extends ObjectWriter<TownBlockMap>
{
	public TownBlockMapWriter(Writers writers)
	{
		super(writers, TownBlockMap.class);
	}
	
	@Override
	protected void writeObject(TownBlockMap townBlockMap) throws IOException
	{
		out.writeInt(townBlockMap.size());
		
		for(TownBlock townBlock : townBlockMap.values())
		{
			writers.getWriter(TownBlockWriter.class).writeReference(townBlock);
		}
	}
}
