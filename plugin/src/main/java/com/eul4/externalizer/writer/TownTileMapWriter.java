package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownTile;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class TownTileMapWriter extends ObjectWriter<Map<Block, TownTile>>
{
	public TownTileMapWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Map<Block, TownTile> townTileMap) throws IOException
	{
		//TODO: write fields...
	}
}
