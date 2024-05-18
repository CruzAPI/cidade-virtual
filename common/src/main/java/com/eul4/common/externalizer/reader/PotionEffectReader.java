package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.Map;

public class PotionEffectReader extends ObjectReader<PotionEffect>
{
	@Getter
	private final Reader<PotionEffect> reader;
	private final Readable<PotionEffect> readable;
	
	public PotionEffectReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PotionEffect.class);
		
		final ObjectType objectType = CommonObjectType.POTION_EFFECT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	@SuppressWarnings("unchecked")
	private PotionEffect readableVersion0() throws IOException, ClassNotFoundException
	{
		return new PotionEffect((Map<String, Object>) in.readObject());
	}
	
	public PotionEffect readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
