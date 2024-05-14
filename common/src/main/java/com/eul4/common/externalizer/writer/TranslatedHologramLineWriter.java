package com.eul4.common.externalizer.writer;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.UUID;

public class TranslatedHologramLineWriter extends ObjectWriter<Hologram.TranslatedHologramLine>
{
	public TranslatedHologramLineWriter(Writers writers)
	{
		super(writers, Hologram.TranslatedHologramLine.class);
	}
	
	@Override
	protected void writeObject(Hologram.TranslatedHologramLine translatedHologramLine) throws IOException
	{
		UUID uuid = translatedHologramLine.getArmorStand().getUniqueId();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
	}
}
