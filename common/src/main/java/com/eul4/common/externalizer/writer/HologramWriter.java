package com.eul4.common.externalizer.writer;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class HologramWriter extends ObjectWriter<Hologram>
{
	public HologramWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Hologram hologram) throws IOException
	{
		writers.getWriter(LocationWriter.class).writeReference(hologram.getLocation());
		
		out.writeInt(hologram.getHologramLines().size());
		
		for(Hologram.TranslatedHologramLine translatedHologramLine : hologram.getHologramLines())
		{
			writers.getWriter(TranslatedHologramLineWriter.class).writeReference(translatedHologramLine);
		}
	}
}
