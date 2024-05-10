package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.entity.ArmorStand;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.UUID;

public class TranslatedHologramLineReader extends ObjectReader<Hologram.TranslatedHologramLine>
{
	private final Reader<Hologram.TranslatedHologramLine> reader;
	private final ParameterizedReadable<Hologram.TranslatedHologramLine, Hologram> parameterizedReadable;
	
	public TranslatedHologramLineReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getTranslatedHologramLineVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TranslatedHologramLine version: " + commonVersions.getTranslatedHologramLineVersion());
		}
	}
	
	private Readable<Hologram.TranslatedHologramLine> parameterizedReadableVersion0(Hologram hologram)
			throws IOException, ClassNotFoundException
	{
		return () ->
		{
			hologram.getLocation().getChunk().load();
			ArmorStand armorStand = (ArmorStand) hologram.getPlugin()
					.getEntityRegisterListener()
					.getEntityByUuid(new UUID(in.readLong(), in.readLong()));
			
			return hologram.new TranslatedHologramLine(armorStand);
		};
	}
	
	private Hologram.TranslatedHologramLine readerVersion0(Hologram.TranslatedHologramLine translatedHologramLine) throws IOException, ClassNotFoundException
	{
		return translatedHologramLine;
	}
	
	public Hologram.TranslatedHologramLine readReference(Hologram hologram) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(hologram));
	}
	
	@Override
	protected Hologram.TranslatedHologramLine readObject(Hologram.TranslatedHologramLine translatedHologramLine) throws IOException, ClassNotFoundException
	{
		return reader.readObject(translatedHologramLine);
	}
}
