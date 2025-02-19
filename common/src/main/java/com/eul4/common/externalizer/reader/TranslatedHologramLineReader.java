package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;

import java.io.IOException;
import java.util.UUID;

public class TranslatedHologramLineReader extends ObjectReader<Hologram.TranslatedHologramLine>
{
	@Getter
	private final Reader<Hologram.TranslatedHologramLine> reader;
	private final ParameterizedReadable<Hologram.TranslatedHologramLine, Hologram> parameterizedReadable;
	
	public TranslatedHologramLineReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Hologram.TranslatedHologramLine.class);
		
		final ObjectType objectType = CommonObjectType.TRANSLATED_HOLOGRAM_LINE;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion1;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Hologram.TranslatedHologramLine> parameterizedReadableVersion0(Hologram hologram)
	{
		return () ->
		{
			hologram.getLocation().getChunk().load();
			ArmorStand armorStand = (ArmorStand) hologram.getPlugin()
					.getServer()
					.getEntity(new UUID(in.readLong(), in.readLong()));
			
			return hologram.new TranslatedHologramLine(armorStand);
		};
	}
	
	private Readable<Hologram.TranslatedHologramLine> parameterizedReadableVersion1(Hologram hologram)
	{
		return () ->
		{
			ArmorStand armorStand = (ArmorStand) readers.getReader(EntityReader.class).readReference(hologram.getPlugin());
			return hologram.new TranslatedHologramLine(armorStand);
		};
	}
	
	public Hologram.TranslatedHologramLine readReference(Hologram hologram) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(hologram));
	}
}
