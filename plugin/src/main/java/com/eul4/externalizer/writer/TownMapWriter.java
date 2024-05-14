package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.Town;
import com.eul4.wrapper.TownMap;

import java.io.IOException;

public class TownMapWriter extends ObjectWriter<TownMap>
{
	public TownMapWriter(Writers writers)
	{
		super(writers, TownMap.class);
	}
	
	@Override
	protected void writeObject(TownMap townMap) throws IOException
	{
		out.writeInt(townMap.size());
		
		for(Town town : townMap.values())
		{
			writers.getWriter(TownWriter.class).writeReference(town);
		}
	}
}
