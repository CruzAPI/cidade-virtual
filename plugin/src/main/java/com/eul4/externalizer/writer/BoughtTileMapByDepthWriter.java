package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownBlock;
import com.eul4.wrapper.BoughtTileMapByDepth;

import java.io.IOException;
import java.util.Map;

public class BoughtTileMapByDepthWriter extends ObjectWriter<BoughtTileMapByDepth>
{
	public BoughtTileMapByDepthWriter(Writers writers)
	{
		super(writers, BoughtTileMapByDepth.class);
	}
	
	@Override
	protected void writeObject(BoughtTileMapByDepth boughtTileMapByDepth) throws IOException
	{
		out.writeInt(boughtTileMapByDepth.size());
		
		for(Map.Entry<Integer, Integer> entry : boughtTileMapByDepth.entrySet())
		{
			final int depth = entry.getKey();
			final int amount = entry.getValue();
			
			out.writeInt(depth);
			out.writeInt(amount);
		}
	}
}
