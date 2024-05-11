package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.Town;

import java.io.IOException;

public class TownWriter extends ObjectWriter<Town>
{
	public TownWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Town object) throws IOException
	{
		//TODO: write fields...
	}
}
