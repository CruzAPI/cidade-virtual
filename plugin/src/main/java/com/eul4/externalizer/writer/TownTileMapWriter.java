package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownTile;
import com.eul4.wrapper.TownTileMap;

import java.io.IOException;

public class TownTileMapWriter extends ObjectWriter<TownTileMap>
{
	public TownTileMapWriter(Writers writers)
	{
		super(writers, TownTileMap.class);
	}
	
	@Override
	protected void writeObject(TownTileMap townTileMap) throws IOException
	{
		out.writeInt(townTileMap.size());
		
		for(TownTile townTile : townTileMap.values())
		{
			writers.getWriter(TownTileWriter.class).writeReference(townTile);
		}
	}
}
