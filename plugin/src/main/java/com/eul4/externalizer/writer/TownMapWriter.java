package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.Town;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class TownMapWriter extends ObjectWriter<Map<UUID, Town>>
{
	public TownMapWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Map<UUID, Town> townMap) throws IOException
	{
		//TODO: write fields...
	}
}
