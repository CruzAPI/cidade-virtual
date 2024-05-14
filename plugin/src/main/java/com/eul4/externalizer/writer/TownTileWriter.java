package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BlockWriter;
import com.eul4.common.externalizer.writer.HologramWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.TownTile;

import java.io.IOException;

public class TownTileWriter extends ObjectWriter<TownTile>
{
	public TownTileWriter(Writers writers)
	{
		super(writers, TownTile.class);
	}
	
	@Override
	protected void writeObject(TownTile townTile) throws IOException
	{
		writers.getWriter(BlockWriter.class).writeReference(townTile.getBlock());
		out.writeBoolean(townTile.isInTownBorder());
		
		out.writeBoolean(townTile.isBought());
		writers.getWriter(HologramWriter.class).writeReference(townTile.getHologram());
	}
}
