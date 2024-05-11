package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownTile;

import java.io.IOException;

public class TownTileWriter extends ObjectWriter<TownTile>
{
	public TownTileWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(TownTile townTile) throws IOException
	{
		//TODO: write fields...
	}
}
