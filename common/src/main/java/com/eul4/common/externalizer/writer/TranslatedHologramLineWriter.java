package com.eul4.common.externalizer.writer;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class TranslatedHologramLineWriter extends ObjectWriter<Hologram.TranslatedHologramLine>
{
	public TranslatedHologramLineWriter(Writers writers)
	{
		super(writers, Hologram.TranslatedHologramLine.class);
	}
	
	@Override
	protected void writeObject(Hologram.TranslatedHologramLine translatedHologramLine) throws IOException
	{
		writers.getWriter(EntityWriter.class).writeReference(translatedHologramLine.getArmorStand());
	}
}
