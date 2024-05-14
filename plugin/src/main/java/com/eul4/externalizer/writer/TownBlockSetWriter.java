package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownBlock;
import com.eul4.wrapper.TownBlockSet;

import java.io.IOException;

public class TownBlockSetWriter extends ObjectWriter<TownBlockSet>
{
	public TownBlockSetWriter(Writers writers)
	{
		super(writers, TownBlockSet.class);
	}
	
	@Override
	protected void writeObject(TownBlockSet townBlockSet) throws IOException
	{
		out.writeInt(townBlockSet.size());
		
		for(TownBlock townBlock : townBlockSet)
		{
			writers.getWriter(TownBlockWriter.class).writeReference(townBlock);
		}
	}
}
